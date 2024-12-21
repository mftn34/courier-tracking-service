package com.furkantufan.courier.tracking.data;

import com.furkantufan.courier.tracking.data.dto.StoreDto;
import com.furkantufan.courier.tracking.entity.Store;

public class StoreData {

    public static Store getStoreEntity(){
        return Store.builder()
                .name("Ataşehir MMM Migros")
                .id(1L)
                .latitude(40.9923307)
                .longitude(29.1244229)
                .build();
    }

    public static StoreDto getStoreDto(){
        return StoreDto.builder()
                .name("Ataşehir MMM Migros")
                .latitude(40.9923307)
                .longitude(29.1244229)
                .build();
    }
}
