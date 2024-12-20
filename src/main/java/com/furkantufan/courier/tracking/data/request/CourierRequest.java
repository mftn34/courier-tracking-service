package com.furkantufan.courier.tracking.data.request;

import com.furkantufan.courier.tracking.data.dto.LocationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class CourierRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -5838734396854959304L;

    @NotNull
    @NotBlank
    private String fullName;

    @NotNull
    @NotBlank
    private String phoneNumber;
}