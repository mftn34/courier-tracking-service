package com.furkantufan.courier.tracking.common.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionDetailDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -6247482540452236954L;

    private String code;

    private String type;

    @JsonIgnore
    private String message;

    @JsonIgnore
    private String localizedMessage;

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("message")
    public String innerMessage() {
        if (StringUtils.isNotBlank(localizedMessage)) {
            return localizedMessage;
        }
        return getMessage();
    }
}
