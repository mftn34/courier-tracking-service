package com.furkantufan.courier.tracking.exception;

import com.furkantufan.courier.tracking.common.exception.BaseException;

import java.io.Serial;

public class CourierException extends BaseException {

    @Serial
    private static final long serialVersionUID = -4429730846005643132L;

    public CourierException(String message) {
        super(message);
    }
}