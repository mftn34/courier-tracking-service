package com.furkantufan.courier.tracking.service;

import com.furkantufan.courier.tracking.data.CourierStoreCache;
import com.furkantufan.courier.tracking.repository.CacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final CacheRepository cacheRepository;

    public void save(String key, Long ttl, String event) {
        if (key != null && ttl != null) {
            var cacheData = CourierStoreCache.builder().id(key).payload(event).expiration(ttl).build();
            cacheRepository.save(cacheData);
        }
    }

    public Optional<CourierStoreCache> getByKey(String key) {
        return cacheRepository.findById(key);
    }
}