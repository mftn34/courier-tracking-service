package com.furkantufan.courier.tracking.mapper;

import com.furkantufan.courier.tracking.common.config.mapstruct.DefaultMapStructConfiguration;
import com.furkantufan.courier.tracking.data.dto.CourierDto;
import com.furkantufan.courier.tracking.data.request.CourierLocationRequest;
import com.furkantufan.courier.tracking.data.request.CourierRequest;
import com.furkantufan.courier.tracking.entity.Courier;
import com.furkantufan.courier.tracking.entity.CourierLocation;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = DefaultMapStructConfiguration.class, builder = @Builder(disableBuilder = true))
public interface CourierMapper {

    CourierDto convert(Courier store);

    List<CourierDto> convert(List<Courier> courierList);

    Courier convert(CourierRequest courierRequest);

    @Mapping(target = "courier", source = "courier")
    @Mapping(target = "latitude", source = "request.location.latitude")
    @Mapping(target = "longitude", source = "request.location.longitude")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    CourierLocation convert(Courier courier, CourierLocationRequest request);
}