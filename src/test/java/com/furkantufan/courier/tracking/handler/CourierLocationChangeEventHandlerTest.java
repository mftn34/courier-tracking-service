package com.furkantufan.courier.tracking.handler;

import com.furkantufan.courier.tracking.configuration.StorePropertiesConfiguration;
import com.furkantufan.courier.tracking.data.CourierData;
import com.furkantufan.courier.tracking.data.CourierStoreCache;
import com.furkantufan.courier.tracking.data.StoreData;
import com.furkantufan.courier.tracking.service.CacheService;
import com.furkantufan.courier.tracking.service.CourierService;
import com.furkantufan.courier.tracking.service.StoreEntranceService;
import com.furkantufan.courier.tracking.service.StoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourierLocationChangeEventHandlerTest {

    @InjectMocks
    private CourierLocationChangeEventHandler eventHandler;

    @Mock
    private StoreService storeService;

    @Mock
    private StorePropertiesConfiguration storeConfiguration;

    @Mock
    private CacheService cacheService;

    @Mock
    private StoreEntranceService storeEntranceService;

    @Mock
    private CourierService courierService;

    @Test
    void shouldProcessEvent_whenCacheMissAndNearestStoreFound() {
        var event = CourierData.getCourierLocationRequest();
        var nearestStore = StoreData.getStoreEntity();
        var courier = CourierData.getCourierEntity();
        var cacheKey = "courier:1--store:1";

        when(storeService.findNearestStoreWithinRadius(anyDouble(), anyDouble(), anyDouble(), any())).thenReturn(nearestStore);

        when(cacheService.getByKey(cacheKey)).thenReturn(Optional.empty());
        when(storeEntranceService.getLastEntranceTime(1L)).thenReturn(Instant.now());

        when(courierService.getReferenceById(1L)).thenReturn(courier);
        when(storeService.getReferenceById(1L)).thenReturn(nearestStore);

        eventHandler.handle(event);

        verify(storeService, times(1)).findNearestStoreWithinRadius(anyDouble(), anyDouble(), anyDouble(), any());

        verify(cacheService, times(1)).getByKey(cacheKey);
        verify(storeEntranceService, times(1)).saveEntrance(eq(courier), eq(nearestStore), anyDouble(), any(Instant.class));
        verify(cacheService, times(1)).save(eq(cacheKey), eq(Duration.ofMinutes(1).getSeconds()), eq(event.toString()));
    }

    @Test
    void shouldNotProcessEvent_whenNearestStoreNotFound() {
        var event = CourierData.getCourierLocationRequest();

        when(storeService.findNearestStoreWithinRadius(anyDouble(), anyDouble(), anyDouble(), any())).thenReturn(null);

        eventHandler.handle(event);

        verify(storeService, times(1)).findNearestStoreWithinRadius(anyDouble(), anyDouble(), anyDouble(), any());

        verifyNoInteractions(cacheService, storeEntranceService, courierService);
    }

    @Test
    void shouldNotProcessEvent_whenCacheHit() {
        var event = CourierData.getCourierLocationRequest();
        var nearestStore = StoreData.getStoreEntity();

        var cacheKey = "courier:1--store:1";

        when(storeService.findNearestStoreWithinRadius(anyDouble(), anyDouble(), anyDouble(), any())).thenReturn(nearestStore);

        when(cacheService.getByKey(cacheKey)).thenReturn(Optional.of(new CourierStoreCache()));

        eventHandler.handle(event);

        verify(storeService, times(1)).findNearestStoreWithinRadius(anyDouble(), anyDouble(), anyDouble(), any());

        verify(cacheService, times(1)).getByKey(cacheKey);
        verifyNoInteractions(storeEntranceService, courierService);
    }

    @Test
    void whenGetLastEntranceTimeIsNull_shouldSetNowTimeAndSaveEntrance() {

        var event = CourierData.getCourierLocationRequest();
        var cacheKey = "courier:1--store:1";
        var nearestStore = StoreData.getStoreEntity();

        when(storeEntranceService.getLastEntranceTime(anyLong())).thenReturn(null);
        when(cacheService.getByKey(cacheKey)).thenReturn(Optional.empty());
        when(storeService.findNearestStoreWithinRadius(anyDouble(), anyDouble(), anyDouble(), any())).thenReturn(nearestStore);

        // When
        eventHandler.handle(event);

        // Then
        verify(storeEntranceService, times(1)).saveEntrance(any(), any(), anyDouble(), any());
        verify(cacheService, times(1)).getByKey(cacheKey);
        verify(storeEntranceService, times(1)).getLastEntranceTime(anyLong());
    }

    @Test
    void whenGetCourierTotalDistanceIsNullOrEmpty_shouldReturnZero() {
        var event = CourierData.getCourierLocationRequest();
        var cacheKey = "courier:1--store:1";
        var nearestStore = StoreData.getStoreEntity();
        var courierId = 1L;
        var lastEntranceTime = Instant.ofEpochSecond(1734775332);
        var pageRequest = PageRequest.of(0, 100);

        when(storeEntranceService.getLastEntranceTime(anyLong())).thenReturn(lastEntranceTime);
        when(cacheService.getByKey(cacheKey)).thenReturn(Optional.empty());
        when(storeService.findNearestStoreWithinRadius(anyDouble(), anyDouble(), anyDouble(), any())).thenReturn(nearestStore);

        when(courierService.getCourierLocationsGreaterThanByTime(courierId, lastEntranceTime, pageRequest)).thenReturn(Page.empty());

        eventHandler.handle(event);

        verify(storeEntranceService, times(1)).saveEntrance(any(), any(), anyDouble(), any());
        verify(cacheService, times(1)).getByKey(cacheKey);
        verify(storeEntranceService, times(1)).getLastEntranceTime(anyLong());
    }

    @Test
    void whenCourierTotalDistanceNotEmpty_shouldUpdateTotalDistanceAndSaveEntrance() {
        var event = CourierData.getCourierLocationRequest();
        var cacheKey = "courier:1--store:1";
        var nearestStore = StoreData.getStoreEntity();
        var courierLocationDto = List.of(CourierData.getCourierLocationDto(), CourierData.getCourierLocationDto2());
        var courierId = 1L;
        var lastEntranceTime = Instant.ofEpochSecond(1734775332);
        var pageRequest = PageRequest.of(0, 100);
        var courierLocationPage = new PageImpl<>(courierLocationDto);

        when(storeEntranceService.getLastEntranceTime(anyLong())).thenReturn(lastEntranceTime);
        when(cacheService.getByKey(cacheKey)).thenReturn(Optional.empty());
        when(storeService.findNearestStoreWithinRadius(anyDouble(), anyDouble(), anyDouble(), any())).thenReturn(nearestStore);

        when(courierService.getCourierLocationsGreaterThanByTime(courierId, lastEntranceTime, pageRequest)).thenReturn(courierLocationPage);

        eventHandler.handle(event);

        verify(storeEntranceService, times(1)).saveEntrance(any(), any(), anyDouble(), any());
        verify(cacheService, times(1)).getByKey(cacheKey);
        verify(storeEntranceService, times(1)).getLastEntranceTime(anyLong());
    }
}