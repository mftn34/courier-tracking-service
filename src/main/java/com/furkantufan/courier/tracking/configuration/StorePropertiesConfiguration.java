package com.furkantufan.courier.tracking.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "store")
public class StorePropertiesConfiguration {

    private Double distanceRadius;
}