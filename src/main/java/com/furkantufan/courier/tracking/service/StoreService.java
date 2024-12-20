package com.furkantufan.courier.tracking.service;

import com.furkantufan.courier.tracking.common.config.i18n.MessageSourceConfiguration;
import com.furkantufan.courier.tracking.data.dto.StoreDto;
import com.furkantufan.courier.tracking.distance.DistanceCalculatorStrategy;
import com.furkantufan.courier.tracking.entity.Store;
import com.furkantufan.courier.tracking.exception.StoreNotFoundException;
import com.furkantufan.courier.tracking.mapper.StoreMapper;
import com.furkantufan.courier.tracking.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    private final StoreMapper storeMapper;

    @Transactional(readOnly = true)
    public StoreDto getStoreDtoById(Long storeId) {
        var store = findByStoreId(storeId);
        return storeMapper.convert(store);
    }

    @Transactional(readOnly = true)
    public Store findByStoreId(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new StoreNotFoundException(
                MessageSourceConfiguration.getMessage("store.not.found", storeId.toString())
        ));
    }

    @Transactional(readOnly = true)
    public Page<StoreDto> getAllStore(int page, int size) {
        var storePage = storeRepository.findAll(PageRequest.of(page, size));
        if (storePage.isEmpty()) {
            return Page.empty();
        }

        var storeDtoList = storeMapper.convert(storePage.getContent());
        return new PageImpl<>(storeDtoList, storePage.getPageable(), storePage.getTotalElements());
    }

    public Store findNearestStoreWithinRadius(double latitude, double longitude, double radius, DistanceCalculatorStrategy calculatorStrategy) {

        return storeRepository.findAll()
                .stream()
                .map(store -> new AbstractMap.SimpleEntry<>(store,
                        calculateDistance(latitude, longitude, store, calculatorStrategy)))
                .filter(entry -> entry.getValue() <= radius)
                .min(Comparator.comparingDouble(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private double calculateDistance(double latitude, double longitude, Store store, DistanceCalculatorStrategy calculatorStrategy) {
        return calculatorStrategy.calculate(latitude, longitude, store.getLatitude(), store.getLongitude());
    }

    public Store getReferenceById(Long storeId) {
        return storeRepository.getReferenceById(storeId);
    }
}