package com.furkantufan.courier.tracking.service;

import com.furkantufan.courier.tracking.data.CourierStoreCache;
import com.furkantufan.courier.tracking.repository.CacheRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CacheServiceTest {

    @InjectMocks
    private CacheService cacheService;

    @Mock
    private CacheRepository cacheRepository;

    @Test
    void shouldSaveCacheData_whenKeyAndTtlAreValid() {
        var key = "testKey";
        var ttl = 60L;
        var event = "testEvent";

        var cacheData = CourierStoreCache.builder()
                .id(key)
                .payload(event)
                .expiration(ttl)
                .build();

        cacheService.save(key, ttl, event);

        verify(cacheRepository, times(1)).save(cacheData);
    }

    @Test
    void shouldNotSaveCacheData_whenKeyIsNull() {
        var ttl = 60L;
        var event = "testEvent";

        cacheService.save(null, ttl, event);

        verifyNoInteractions(cacheRepository);
    }

    @Test
    void shouldReturnEmptyOptional_whenKeyDoesNotExist() {
        var key = "testKey";

        when(cacheRepository.findById(key)).thenReturn(Optional.empty());

        var result = cacheService.getByKey(key);

        assertFalse(result.isPresent());
        verify(cacheRepository, times(1)).findById(key);
    }

    @Test
    void shouldReturnCacheData_whenKeyExists() {
        var key = "testKey";
        var cacheData = CourierStoreCache.builder()
                .id(key)
                .payload("testEvent")
                .expiration(3600L)
                .build();

        when(cacheRepository.findById(key)).thenReturn(Optional.of(cacheData));

        var result = cacheService.getByKey(key);

        assertTrue(result.isPresent());
        assertEquals(cacheData, result.get());
        verify(cacheRepository, times(1)).findById(key);
    }

    @Test
    void shouldNotSaveCacheData_whenTtlIsNull() {
        var event = "testEvent";
        var key = "testKey";

        cacheService.save(key, null, event);

        verifyNoInteractions(cacheRepository);
    }
}