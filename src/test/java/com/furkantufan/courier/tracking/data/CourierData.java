package com.furkantufan.courier.tracking.data;

import com.furkantufan.courier.tracking.data.dto.CourierDto;
import com.furkantufan.courier.tracking.data.dto.CourierLocationDto;
import com.furkantufan.courier.tracking.data.dto.LocationDto;
import com.furkantufan.courier.tracking.data.request.CourierLocationRequest;
import com.furkantufan.courier.tracking.data.request.CourierRequest;
import com.furkantufan.courier.tracking.entity.Courier;
import com.furkantufan.courier.tracking.entity.CourierLocation;

import java.time.Instant;

public class CourierData {

    public static Courier getCourierEntity() {
        return Courier.builder().fullName("Test Kurye").id(1L).phoneNumber("05554557474").build();
    }

    public static CourierDto getCourierDto() {
        return CourierDto.builder().fullName("Test Kurye").phoneNumber("05554557474").build();
    }

    public static CourierRequest getCourierRequest() {
        return CourierRequest.builder().fullName("Test Kurye").phoneNumber("05551114477").build();
    }

    public static CourierLocationRequest getCourierLocationRequest() {
        return CourierLocationRequest.builder().location(getLocation()).courierId(1L).time(Instant.ofEpochSecond(1734775332)).build();
    }

    private static LocationDto getLocation() {
        return LocationDto.builder().latitude(40.88).longitude(30.22).build();
    }

    public static CourierLocation getCourierLocationEntity() {
        return CourierLocation.builder().courier(getCourierEntity()).id(1L).latitude(40.99).longitude(30.66).time(Instant.ofEpochSecond(1734775332)).build();
    }

    public static CourierLocationDto getCourierLocationDto() {
        return CourierLocationDto.builder().latitude(40.99).longitude(30.66).time(Instant.ofEpochSecond(1734775332)).build();
    }

    public static CourierLocationDto getCourierLocationDto2() {
        return CourierLocationDto.builder().latitude(41.49).longitude(31.33).time(Instant.ofEpochSecond(1734775332)).build();
    }
}