package com.furkantufan.courier.tracking.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LocationDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1167652815288108728L;

    @NotNull
    @JsonProperty("lat")
    private Double latitude;

    @NotNull
    @JsonProperty("lng")
    private Double longitude;
}