package com.furkantufan.courier.tracking.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class CourierLocationDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 5725933655336386577L;

    private Double latitude;

    private Double longitude;

    private Instant time;
}