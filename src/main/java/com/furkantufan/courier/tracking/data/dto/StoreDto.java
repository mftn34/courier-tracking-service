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
public class StoreDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 7892189155999297579L;

    private String id;

    private String name;

    private Double latitude;

    private Double longitude;
}