package com.furkantufan.courier.tracking.mapper;

import com.furkantufan.courier.tracking.common.config.mapstruct.DefaultMapStructConfiguration;
import com.furkantufan.courier.tracking.entity.Courier;
import com.furkantufan.courier.tracking.entity.Store;
import com.furkantufan.courier.tracking.entity.StoreEntrance;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(config = DefaultMapStructConfiguration.class, builder = @Builder(disableBuilder = true))
public interface StoreEntranceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "date", expression = "java(getCurrentDate())")
    StoreEntrance convert(Courier courier, Store store, Double totalDistance, Instant entranceTime);

    default Instant getCurrentDate() {
        return Instant.now();
    }
}
