package com.furkantufan.courier.tracking.service;

import com.furkantufan.courier.tracking.entity.Courier;
import com.furkantufan.courier.tracking.entity.Store;
import com.furkantufan.courier.tracking.mapper.StoreEntranceMapper;
import com.furkantufan.courier.tracking.repository.StoreEntranceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class StoreEntranceService {

    private final StoreEntranceRepository storeEntranceRepository;

    private final StoreEntranceMapper storeEntranceMapper;

    @Transactional()
    public void saveEntrance(Courier courierReference, Store storeReference, Double totalDistance, Instant lastEntranceTime) {
        var storeEntrance = storeEntranceMapper.convert(courierReference, storeReference, totalDistance, lastEntranceTime);
        storeEntranceRepository.save(storeEntrance);
    }

    @Transactional(readOnly = true)
    public Double getTotalTravelDistanceByCourierId(Long courierId) {
        return storeEntranceRepository.getTotalTravelDistanceByCourierId(courierId).orElse(null);
    }

    @Transactional(readOnly = true)
    public Instant getLastEntranceTime(Long courierId) {
        var entrance = storeEntranceRepository.getLastEntranceTime(courierId);
        if (CollectionUtils.isEmpty(entrance)) {
            return null;
        }
        return entrance.getFirst();
    }
}