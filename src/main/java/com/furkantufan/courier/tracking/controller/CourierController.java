package com.furkantufan.courier.tracking.controller;

import com.furkantufan.courier.tracking.data.dto.CourierDistanceDto;
import com.furkantufan.courier.tracking.data.dto.CourierDto;
import com.furkantufan.courier.tracking.data.request.CourierLocationRequest;
import com.furkantufan.courier.tracking.data.request.CourierRequest;
import com.furkantufan.courier.tracking.service.CourierService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/couriers", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class CourierController {

    private final CourierService courierService;

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get courier by id")
    public CourierDto getById(@PathVariable("id") @NotNull(message = "id cannot be null") Long id) {
        return courierService.getCourierDtoById(id);
    }

    @GetMapping(value = "/all")
    @Operation(summary = "Get all courier with pageable")
    public Page<CourierDto> getAllCourier(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "50") int size) {
        return courierService.getAllCourier(page, size);
    }

    @GetMapping("/phone-number/{phone-number}")
    @Operation(summary = "Get courier by phone number")
    public CourierDto getCourierByPhoneNumber(@PathVariable("phone-number") @NotNull(message = "phone-number cannot be null") String phoneNumber) {
        return courierService.getCourierByPhoneNumber(phoneNumber);
    }

    @GetMapping("/total-distances/{courier-id}")
    @Operation(summary = "Get courier total travel distance.", description = "The returned distance is in meters.")
    public CourierDistanceDto getTotalDistance(@PathVariable("courier-id") Long courierId) {
        return courierService.getTotalDistance(courierId);
    }

    @PostMapping("/location")
    @Operation(summary = "Courier location information save")
    public ResponseEntity<Void> saveLocation(@Valid @RequestBody CourierLocationRequest courierLocationRequest) {
        courierService.saveLocation(courierLocationRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @Operation(summary = "Create a new courier or update exist couriers")
    public ResponseEntity<Void> saveOrUpdate(@RequestBody @Valid CourierRequest courierRequest) {
        courierService.saveOrUpdateCourier(courierRequest);
        return ResponseEntity.ok().build();
    }
}