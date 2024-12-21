package com.furkantufan.courier.tracking.data;

import com.furkantufan.courier.tracking.entity.StoreEntrance;

import java.time.Instant;

public class StoreEntranceData {

    public static StoreEntrance getStoreEntranceEntity() {
        return StoreEntrance.builder()
                .store(StoreData.getStoreEntity())
                .totalDistance(100.0)
                .date(Instant.ofEpochSecond(1734775332))
                .entranceTime(Instant.ofEpochSecond(1734775332))
                .courier(CourierData.getCourierEntity())
                .build();
    }
}
