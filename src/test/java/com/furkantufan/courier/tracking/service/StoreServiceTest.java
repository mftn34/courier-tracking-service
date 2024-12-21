package com.furkantufan.courier.tracking.service;

import com.furkantufan.courier.tracking.data.StoreData;
import com.furkantufan.courier.tracking.distance.DistanceCalculatorStrategy;
import com.furkantufan.courier.tracking.exception.StoreNotFoundException;
import com.furkantufan.courier.tracking.mapper.StoreMapper;
import com.furkantufan.courier.tracking.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreMapper storeMapper;

    @InjectMocks
    private StoreService storeService;

    @Test
    void shouldReturnStoreDto_whenStoreExists() {
        var storeId = 1L;
        var store = StoreData.getStoreEntity();
        var storeDto = StoreData.getStoreDto();

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(storeMapper.convert(store)).thenReturn(storeDto);

        var result = storeService.getStoreDtoById(storeId);

        assertEquals(storeDto, result);
        verify(storeRepository).findById(storeId);
        verify(storeMapper).convert(store);
    }

    @Test
    void shouldThrowException_whenStoreDoesNotExist() {
        var storeId = 1L;

        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        assertThrows(StoreNotFoundException.class, () -> storeService.getStoreDtoById(storeId));
        verify(storeRepository).findById(storeId);
    }

    @Test
    void shouldReturnPageOfStoreDto_WhenStoresExist() {
        int page = 0;
        int size = 10;

        var store = StoreData.getStoreEntity();
        var storeDto = StoreData.getStoreDto();
        var storeList = List.of(store);
        var storePage = new PageImpl<>(storeList);

        when(storeRepository.findAll(PageRequest.of(page, size))).thenReturn(storePage);
        when(storeMapper.convert(storeList)).thenReturn(List.of(storeDto));

        var result = storeService.getAllStore(page, size);

        assertEquals(1, result.getTotalElements());
        assertEquals(storeDto, result.getContent().getFirst());
        verify(storeRepository).findAll(PageRequest.of(page, size));
        verify(storeMapper).convert(storeList);
    }

    @Test
    void shouldReturnEmptyPage_whenNoStoresExist() {
        int page = 0;
        int size = 10;

        when(storeRepository.findAll(PageRequest.of(page, size))).thenReturn(Page.empty());

        var result = storeService.getAllStore(page, size);

        assertTrue(result.isEmpty());
        verify(storeRepository).findAll(PageRequest.of(page, size));
    }

    @Test
    void shouldReturnNearestStore_whenStoreExistsWithinRadius() {
        double latitude = 40.0;
        double longitude = 29.0;
        double radius = 10.0;

        var store = StoreData.getStoreEntity();

        DistanceCalculatorStrategy calculatorStrategy = mock(DistanceCalculatorStrategy.class);

        when(storeRepository.findAll()).thenReturn(List.of(store));
        when(calculatorStrategy.calculate(latitude, longitude, store.getLatitude(), store.getLongitude()))
                .thenReturn(5.0);

        var result = storeService.findNearestStoreWithinRadius(latitude, longitude, radius, calculatorStrategy);

        assertEquals(store, result);
        verify(storeRepository).findAll();
        verify(calculatorStrategy).calculate(latitude, longitude, store.getLatitude(), store.getLongitude());
    }

    @Test
    void shouldReturnNull_whenNoStoreExistsWithinRadius() {
        double latitude = 40.0;
        double longitude = 29.0;
        double radius = 10.0;

        var store = StoreData.getStoreEntity();

        DistanceCalculatorStrategy calculatorStrategy = mock(DistanceCalculatorStrategy.class);
        when(storeRepository.findAll()).thenReturn(List.of(store));
        when(calculatorStrategy.calculate(latitude, longitude, store.getLatitude(), store.getLongitude()))
                .thenReturn(15.0);

        var result = storeService.findNearestStoreWithinRadius(latitude, longitude, radius, calculatorStrategy);

        assertNull(result);
        verify(storeRepository).findAll();
        verify(calculatorStrategy).calculate(latitude, longitude, store.getLatitude(), store.getLongitude());
    }

    @Test
    void shouldReturnStoreReference() {

        var store = StoreData.getStoreEntity();

        when(storeRepository.getReferenceById(store.getId())).thenReturn(store);

        var result = storeService.getReferenceById(store.getId());

        assertEquals(result.getId(), store.getId());
        verify(storeRepository).getReferenceById(store.getId());
    }
}