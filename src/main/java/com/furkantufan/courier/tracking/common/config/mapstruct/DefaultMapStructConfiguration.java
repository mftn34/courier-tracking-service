package com.furkantufan.courier.tracking.common.config.mapstruct;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel;

@MapperConfig(
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = ComponentModel.SPRING
)
public interface DefaultMapStructConfiguration {
}
