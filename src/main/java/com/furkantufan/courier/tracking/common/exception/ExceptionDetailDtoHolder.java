package com.furkantufan.courier.tracking.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionDetailDtoHolder implements Serializable {

    @Serial
    private static final long serialVersionUID = -8629818870435377322L;

    private String message;

    private List<ExceptionDetailDto> details;
}
