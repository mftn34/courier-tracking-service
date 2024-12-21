package com.furkantufan.courier.tracking.service;

import com.furkantufan.courier.tracking.common.config.i18n.MessageSourceConfiguration;
import com.furkantufan.courier.tracking.data.dto.CourierDistanceDto;
import com.furkantufan.courier.tracking.data.dto.CourierDto;
import com.furkantufan.courier.tracking.data.dto.CourierLocationDto;
import com.furkantufan.courier.tracking.data.request.CourierLocationRequest;
import com.furkantufan.courier.tracking.data.request.CourierRequest;
import com.furkantufan.courier.tracking.entity.Courier;
import com.furkantufan.courier.tracking.exception.CourierException;
import com.furkantufan.courier.tracking.mapper.CourierMapper;
import com.furkantufan.courier.tracking.repository.CourierLocationRepository;
import com.furkantufan.courier.tracking.repository.CourierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CourierService {

    private final CourierRepository courierRepository;

    private final CourierLocationRepository courierLocationRepository;

    private final CourierMapper courierMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final StoreEntranceService storeEntranceService;

    @Transactional(readOnly = true)
    public CourierDto getCourierDtoById(Long id) {
        var courier = findByCourierId(id);
        return courierMapper.convert(courier);
    }

    @Transactional(readOnly = true)
    public Courier findByCourierId(Long id) {
        return courierRepository.findById(id).orElseThrow(() -> new CourierException(
                MessageSourceConfiguration.getMessage("courier.not.found",
                        String.valueOf(id)))
        );
    }

    @Transactional(readOnly = true)
    public CourierDto getCourierByPhoneNumber(String phoneNumber) {
        var courier = courierRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new CourierException(
                MessageSourceConfiguration.getMessage("courier.not.found", phoneNumber)
        ));

        return courierMapper.convert(courier);
    }

    @Transactional(readOnly = true)
    public Page<CourierDto> getAllCourier(int page, int size) {
        var courierPage = courierRepository.findAll(PageRequest.of(page, size));
        if (courierPage.isEmpty()) {
            return Page.empty();
        }

        var courierDtoList = courierMapper.convert(courierPage.getContent());
        return new PageImpl<>(courierDtoList, courierPage.getPageable(), courierPage.getTotalElements());
    }

    public Courier getReferenceById(Long id) {
        return courierRepository.getReferenceById(id);
    }

    @Transactional(readOnly = true)
    public Page<CourierLocationDto> getCourierLocationsGreaterThanByTime(Long courierId, Instant lastEntranceTime, PageRequest pageRequest) {
        return courierRepository.getCourierLocationsGreaterThanByTime(courierId, lastEntranceTime, pageRequest);
    }

    @Transactional
    public void saveOrUpdateCourier(CourierRequest courierRequest) {
        var optionalCourier = courierRepository.findByPhoneNumber(courierRequest.getPhoneNumber());
        optionalCourier.ifPresentOrElse(existingCourier ->
                        existingCourier.setFullName(courierRequest.getFullName()),
                () -> saveNewCourier(courierRequest));
    }

    private void saveNewCourier(CourierRequest courierRequest) {
        var entity = courierMapper.convert(courierRequest);
        courierRepository.save(entity);
    }

    @Transactional
    public void saveLocation(CourierLocationRequest courierLocationRequest) {
        var courierId = courierLocationRequest.getCourierId();
        var courier = findByCourierId(courierId);
        if (courier.getDeleted()) {
            throw new CourierException(MessageSourceConfiguration.getMessage("courier.is.not.active",
                    courierId.toString()));
        }
        var courierLocation = courierMapper.convert(courier, courierLocationRequest);
        courierLocationRepository.save(courierLocation);
        applicationEventPublisher.publishEvent(courierLocationRequest);
    }

    @Transactional(readOnly = true)
    public CourierDistanceDto getTotalDistance(Long courierId) {
        var totalDistance = storeEntranceService.getTotalTravelDistanceByCourierId(courierId);
        if (totalDistance == null) {
            throw new CourierException(
                    MessageSourceConfiguration.getMessage("courier.or.distance.data.not.found.",
                            String.valueOf(courierId)));
        }
        return CourierDistanceDto.builder().totalDistance(totalDistance).build();
    }
}