package com.furkantufan.courier.tracking.data;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "courierStoreCache")
public class CourierStoreCache {

    @Id
    private String id;

    private String payload;

    @TimeToLive
    private Long expiration;
}