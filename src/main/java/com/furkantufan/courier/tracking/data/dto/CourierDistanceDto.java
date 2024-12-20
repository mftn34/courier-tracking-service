package com.furkantufan.courier.tracking.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CourierDistanceDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 6367840918243258288L;

    private Double totalDistance;
}