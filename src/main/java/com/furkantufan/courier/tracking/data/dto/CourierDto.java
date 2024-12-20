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
public class CourierDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 4733402322210455694L;

    private String id;

    private String fullName;

    private String phoneNumber;
}