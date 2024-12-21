package com.furkantufan.courier.tracking.service;

import com.furkantufan.courier.tracking.data.CourierData;
import com.furkantufan.courier.tracking.exception.CourierException;
import com.furkantufan.courier.tracking.mapper.CourierMapper;
import com.furkantufan.courier.tracking.repository.CourierLocationRepository;
import com.furkantufan.courier.tracking.repository.CourierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

    @InjectMocks
    private CourierService courierService;

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private CourierMapper courierMapper;

    @Mock
    private CourierLocationRepository courierLocationRepository;

    @Mock
    private StoreEntranceService storeEntranceService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    void shouldThrowException_whenCourierDoesNotExist() {
        var courierId = CourierData.getCourierEntity().getId();

        when(courierRepository.findById(courierId)).thenReturn(Optional.empty());

        assertThrows(CourierException.class, () -> courierService.getCourierDtoById(courierId));
        verify(courierRepository, times(1)).findById(courierId);
    }

    @Test
    void shouldReturnCourierDto_whenCourierExists() {
        var courier = CourierData.getCourierEntity();
        var courierId = courier.getId();
        var courierDto = CourierData.getCourierDto();

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));
        when(courierMapper.convert(courier)).thenReturn(courierDto);

        var result = courierService.getCourierDtoById(courierId);

        assertEquals(courierDto, result);
        verify(courierRepository, times(1)).findById(courierId);
        verify(courierMapper, times(1)).convert(courier);
    }

    @Test
    void shouldReturnCourierDto_whenGetCourierByPhoneNumberAndCourierExists() {
        var courier = CourierData.getCourierEntity();
        var courierDto = CourierData.getCourierDto();
        var phoneNumber = "05554557474";

        when(courierRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(courier));
        when(courierMapper.convert(courier)).thenReturn(courierDto);

        var result = courierService.getCourierByPhoneNumber(phoneNumber);

        assertEquals(courierDto, result);
        verify(courierRepository, times(1)).findByPhoneNumber(phoneNumber);
        verify(courierMapper, times(1)).convert(courier);
    }

    @Test
    void shouldUpdateExistingCourier_whenCourierExists() {
        var courierRequest = CourierData.getCourierRequest();
        var existingCourier = CourierData.getCourierEntity();

        when(courierRepository.findByPhoneNumber(courierRequest.getPhoneNumber())).thenReturn(Optional.of(existingCourier));

        courierService.saveOrUpdateCourier(courierRequest);

        assertEquals(1L, existingCourier.getId());
        verify(courierRepository, times(1)).findByPhoneNumber(courierRequest.getPhoneNumber());
    }

    @Test
    void shouldThrowException_whenGetCourierByPhoneNumberCourierDoesNotExist() {
        var phoneNumber = "05559998877";

        when(courierRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());

        assertThrows(CourierException.class, () -> courierService.getCourierByPhoneNumber(phoneNumber));
        verify(courierRepository, times(1)).findByPhoneNumber(phoneNumber);
    }

    @Test
    void shouldSaveNewCourier_whenCourierDoesNotExist() {
        var courierRequest = CourierData.getCourierRequest();
        var courier = CourierData.getCourierEntity();

        when(courierRepository.findByPhoneNumber(courierRequest.getPhoneNumber())).thenReturn(Optional.empty());
        when(courierMapper.convert(courierRequest)).thenReturn(courier);

        courierService.saveOrUpdateCourier(courierRequest);

        verify(courierRepository, times(1)).findByPhoneNumber(courierRequest.getPhoneNumber());
        verify(courierMapper, times(1)).convert(courierRequest);
        verify(courierRepository, times(1)).save(courier);
    }

    @Test
    void shouldSaveLocationAndPublishEvent_whenCourierIsActive() {
        var courierLocationRequest = CourierData.getCourierLocationRequest();
        var courier = CourierData.getCourierEntity();
        var courierLocation = CourierData.getCourierLocationEntity();


        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));
        when(courierMapper.convert(courier, courierLocationRequest)).thenReturn(courierLocation);

        courierService.saveLocation(courierLocationRequest);

        verify(courierLocationRepository, times(1)).save(courierLocation);
        verify(applicationEventPublisher, times(1)).publishEvent(courierLocationRequest);
    }

    @Test
    void shouldThrowException_whenCourierIsDeleted() {
        var courierLocationRequest = CourierData.getCourierLocationRequest();
        var courier = CourierData.getCourierEntity();
        courier.setDeleted(true);

        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));

        assertThrows(CourierException.class, () -> courierService.saveLocation(courierLocationRequest));

        verifyNoInteractions(courierLocationRepository);
        verifyNoInteractions(applicationEventPublisher);
    }

    @Test
    void getReferenceById_shouldReturnCourier() {
        var courier = CourierData.getCourierEntity();

        when(courierRepository.getReferenceById(1L)).thenReturn(courier);

        var result = courierService.getReferenceById(courier.getId());

        assertNotNull(result);
        assertEquals(courier, result);
        verify(courierRepository, times(1)).getReferenceById(1L);
    }

    @Test
    void shouldReturnPageOfAllCourierDto_whenCouriersExist() {
        int page = 0;
        int size = 10;
        var pageRequest = PageRequest.of(page, size);
        var courier = CourierData.getCourierEntity();
        var courierDto = CourierData.getCourierDto();
        var courierPage = new PageImpl<>(Collections.singletonList(courier), pageRequest, 1);

        when(courierRepository.findAll(pageRequest)).thenReturn(courierPage);
        when(courierMapper.convert(Collections.singletonList(courier))).thenReturn(Collections.singletonList(courierDto));

        var result = courierService.getAllCourier(page, size);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        verify(courierRepository, times(1)).findAll(pageRequest);
        verify(courierMapper, times(1)).convert(Collections.singletonList(courier));
    }

    @Test
    void shouldReturnEmptyPage_whenNoCouriersExist() {
        int page = 0;
        int size = 10;
        var pageRequest = PageRequest.of(page, size);

        when(courierRepository.findAll(pageRequest)).thenReturn(Page.empty());

        var result = courierService.getAllCourier(page, size);

        assertTrue(result.isEmpty());
        verify(courierRepository, times(1)).findAll(pageRequest);
    }

    @Test
    void getTotalDistance_ShouldReturnCourierDistanceDto_whenDistanceExists() {
        Long courierId = 1L;
        double totalDistance = 100.0;

        when(storeEntranceService.getTotalTravelDistanceByCourierId(courierId)).thenReturn(totalDistance);

        var result = courierService.getTotalDistance(courierId);

        assertNotNull(result);
        assertEquals(totalDistance, result.getTotalDistance());
        verify(storeEntranceService, times(1)).getTotalTravelDistanceByCourierId(courierId);
    }

    @Test
    void shouldThrowException_whenTotalDistanceIsNull() {
        var courierId = 1L;

        when(storeEntranceService.getTotalTravelDistanceByCourierId(courierId)).thenReturn(null);

        assertThrows(CourierException.class, () -> courierService.getTotalDistance(courierId));
        verify(storeEntranceService, times(1)).getTotalTravelDistanceByCourierId(courierId);
    }

    @Test
    void getCourierLocationsGreaterThanByTime_shouldReturnPageOfCourierLocationDto() {
        var courierLocationDto = CourierData.getCourierLocationDto();
        var courierId = 1L;
        var lastEntranceTime = Instant.ofEpochSecond(1734775332);
        var pageRequest = PageRequest.of(0, 10);
        var courierLocationPage = new PageImpl<>(Collections.singletonList(courierLocationDto));

        when(courierRepository.getCourierLocationsGreaterThanByTime(courierId, lastEntranceTime, pageRequest)).thenReturn(courierLocationPage);

        var result = courierService.getCourierLocationsGreaterThanByTime(courierId, lastEntranceTime, pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(courierRepository, times(1)).getCourierLocationsGreaterThanByTime(courierId, lastEntranceTime, pageRequest);
    }
}