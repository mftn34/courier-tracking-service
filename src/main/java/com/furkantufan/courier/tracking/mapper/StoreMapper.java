package com.furkantufan.courier.tracking.mapper;

import com.furkantufan.courier.tracking.common.config.mapstruct.DefaultMapStructConfiguration;
import com.furkantufan.courier.tracking.data.dto.StoreDto;
import com.furkantufan.courier.tracking.entity.Store;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = DefaultMapStructConfiguration.class, builder = @Builder(disableBuilder = true))
public interface StoreMapper {

    StoreDto convert(Store store);

    List<StoreDto> convert(List<Store> storeList);
}