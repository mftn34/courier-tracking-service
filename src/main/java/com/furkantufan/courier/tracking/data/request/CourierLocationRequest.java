package com.furkantufan.courier.tracking.data.request;

import com.furkantufan.courier.tracking.data.dto.LocationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CourierLocationRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 6646636618357525163L;

    @NotNull
    @Positive
    private Long courierId;

    @Valid
    @NotNull
    private LocationDto location;

    @NotNull
    private Instant time;
}