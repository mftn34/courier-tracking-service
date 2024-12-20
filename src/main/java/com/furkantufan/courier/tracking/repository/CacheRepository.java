package com.furkantufan.courier.tracking.repository;

import com.furkantufan.courier.tracking.data.CourierStoreCache;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP,
        keyspaceNotificationsConfigParameter = "")
public interface CacheRepository extends CrudRepository<CourierStoreCache, String> {
}
