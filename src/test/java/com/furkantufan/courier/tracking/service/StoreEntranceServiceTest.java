package com.furkantufan.courier.tracking.service;

import com.furkantufan.courier.tracking.data.CourierData;
import com.furkantufan.courier.tracking.data.StoreData;
import com.furkantufan.courier.tracking.data.StoreEntranceData;
import com.furkantufan.courier.tracking.mapper.StoreEntranceMapper;
import com.furkantufan.courier.tracking.repository.StoreEntranceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreEntranceServiceTest {

    @Mock
    private StoreEntranceRepository storeEntranceRepository;

    @Mock
    private StoreEntranceMapper storeEntranceMapper;

    @InjectMocks
    private StoreEntranceService storeEntranceService;

    @Test
    void shouldReturnTotalDistance_whenCourierIdExists() {
        var courierId = CourierData.getCourierEntity().getId();
        var totalDistance = StoreEntranceData.getStoreEntranceEntity().getTotalDistance();

        when(storeEntranceRepository.getTotalTravelDistanceByCourierId(courierId)).thenReturn(Optional.of(totalDistance));

        var result = storeEntranceService.getTotalTravelDistanceByCourierId(courierId);

        assertEquals(totalDistance, result);
        verify(storeEntranceRepository, times(1)).getTotalTravelDistanceByCourierId(courierId);
    }

    @Test
    void shouldReturnNull_whenCourierIdDoesNotExist() {
        var courierId = CourierData.getCourierEntity().getId();

        when(storeEntranceRepository.getTotalTravelDistanceByCourierId(courierId)).thenReturn(Optional.empty());

        var result = storeEntranceService.getTotalTravelDistanceByCourierId(courierId);

        assertNull(result);
        verify(storeEntranceRepository, times(1)).getTotalTravelDistanceByCourierId(courierId);
    }

    @Test
    void shouldReturnNull_whenEntranceDoesNotExist() {
        var courierId = CourierData.getCourierEntity().getId();

        when(storeEntranceRepository.getLastEntranceTime(courierId)).thenReturn(Collections.emptyList());

        var result = storeEntranceService.getLastEntranceTime(courierId);

        assertNull(result);
        verify(storeEntranceRepository, times(1)).getLastEntranceTime(courierId);
    }

    @Test
    void shouldReturnLastEntranceTime_whenEntranceExists() {
        var courierId = CourierData.getCourierEntity().getId();
        var lastEntranceTime = StoreEntranceData.getStoreEntranceEntity().getEntranceTime();
        var entranceList = List.of(lastEntranceTime);

        when(storeEntranceRepository.getLastEntranceTime(courierId)).thenReturn(entranceList);

        var result = storeEntranceService.getLastEntranceTime(courierId);

        assertEquals(lastEntranceTime, result);
        verify(storeEntranceRepository, times(1)).getLastEntranceTime(courierId);
    }

    @Test
    void shouldSaveStoreEntrance_whenValidInput() {
        var courier = CourierData.getCourierEntity();
        var store = StoreData.getStoreEntity();
        var storeEntrance = StoreEntranceData.getStoreEntranceEntity();
        var totalDistance = storeEntrance.getTotalDistance();
        var lastEntranceTime = storeEntrance.getEntranceTime();

        when(storeEntranceMapper.convert(courier, store, totalDistance, lastEntranceTime)).thenReturn(storeEntrance);

        storeEntranceService.saveEntrance(courier, store, totalDistance, lastEntranceTime);

        verify(storeEntranceMapper, times(1)).convert(courier, store, totalDistance, lastEntranceTime);
        verify(storeEntranceRepository, times(1)).save(storeEntrance);
    }
}