package com.furkantufan.courier.tracking.handler;

import com.furkantufan.courier.tracking.common.util.DateUtil;
import com.furkantufan.courier.tracking.configuration.StorePropertiesConfiguration;
import com.furkantufan.courier.tracking.data.dto.CourierLocationDto;
import com.furkantufan.courier.tracking.data.request.CourierLocationRequest;
import com.furkantufan.courier.tracking.distance.DistanceCalculatorStrategy;
import com.furkantufan.courier.tracking.distance.HaversineDistanceCalculator;
import com.furkantufan.courier.tracking.entity.Store;
import com.furkantufan.courier.tracking.service.CacheService;
import com.furkantufan.courier.tracking.service.CourierService;
import com.furkantufan.courier.tracking.service.StoreEntranceService;
import com.furkantufan.courier.tracking.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Streamable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.furkantufan.courier.tracking.common.async.AsyncConfiguration.THREAD_EXECUTOR_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourierLocationChangeEventHandler {

    private final StoreService storeService;

    private final StorePropertiesConfiguration storeConfiguration;

    private final CacheService cacheService;

    private final StoreEntranceService storeEntranceService;

    private final CourierService courierService;

    @Async(THREAD_EXECUTOR_NAME)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(CourierLocationRequest event) {

        var nearestStore = findNearestStore(event);
        if (nearestStore == null) {
            return;
        }

        var cacheKey = createCacheKey(event, nearestStore);
        if (!isWithinCache(cacheKey)) {
            var lastEntranceTime = getLastEntranceTime(event);
            var courier = courierService.getReferenceById(event.getCourierId());
            var store = storeService.getReferenceById(nearestStore.getId());
            var totalDistance = getCourierTotalDistance(getCourierLocations(event.getCourierId(), lastEntranceTime));
            storeEntranceService.saveEntrance(courier, store, totalDistance, lastEntranceTime);
            saveCache(cacheKey, event);
            log.info(" Store Entrance: {} - Courier id: {} - Courier total distance:{}m ",
                    nearestStore.getName(), event.getCourierId(), totalDistance);
        }

    }

    private Store findNearestStore(CourierLocationRequest event) {
        return storeService.findNearestStoreWithinRadius(
                event.getLocation().getLatitude(),
                event.getLocation().getLongitude(),
                storeConfiguration.getDistanceRadius(),
                getCalculationStrategy()
        );
    }

    private String createCacheKey(CourierLocationRequest event, Store nearestStore) {
        return String.format("courier:%s--store:%s", event.getCourierId(), nearestStore.getId());
    }

    private boolean isWithinCache(String cacheKey) {
        return cacheService.getByKey(cacheKey).isPresent();
    }

    private void saveCache(String cacheKey, CourierLocationRequest event) {
        cacheService.save(cacheKey, Duration.ofMinutes(1).getSeconds(), event.toString());
    }

    private Instant getLastEntranceTime(CourierLocationRequest event) {
        var lastEntranceTime = storeEntranceService.getLastEntranceTime(event.getCourierId());
        if (lastEntranceTime == null) {
            lastEntranceTime = DateUtil.getStartOfDayInstantAtUTC();
        }
        return lastEntranceTime;
    }

    private Double getCourierTotalDistance(List<CourierLocationDto> locations) {
        var calculationStrategy = getCalculationStrategy();

        if (locations.isEmpty()) {
            return 0d;
        }

        return IntStream.range(0, locations.size() - 1)
                .mapToDouble(i -> {
                    var firstLocation = locations.get(i);
                    var secondLocation = locations.get(i + 1);

                    var firstLat = firstLocation.getLatitude();
                    var firstLng = firstLocation.getLongitude();
                    var secondLat = secondLocation.getLatitude();
                    var secondLng = secondLocation.getLongitude();

                    return calculationStrategy.calculate(firstLat, firstLng, secondLat, secondLng);
                })
                .sum();
    }

    private List<CourierLocationDto> getCourierLocations(Long courierId, Instant lastEntranceTime) {
        var firstPage = courierService.getCourierLocationsGreaterThanByTime(courierId,
                lastEntranceTime,
                PageRequest.of(0, 100));

        if (firstPage == null || firstPage.isEmpty()) {
            return Collections.emptyList();
        }

        return Stream.iterate(firstPage, Objects::nonNull,
                        page -> {
                            int nextPageNumber = page.getNumber() + 1;
                            return nextPageNumber <= page.getTotalPages()
                                    ? courierService.getCourierLocationsGreaterThanByTime(courierId, lastEntranceTime, PageRequest.of(nextPageNumber, page.getSize()))
                                    : Page.empty();
                        })
                .flatMap(Streamable::get)
                .sorted(Comparator.comparing(CourierLocationDto::getTime))
                .toList();
    }

    private DistanceCalculatorStrategy getCalculationStrategy() {
        return new DistanceCalculatorStrategy(new HaversineDistanceCalculator());
    }
}