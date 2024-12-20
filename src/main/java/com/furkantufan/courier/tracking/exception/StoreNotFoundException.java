package com.furkantufan.courier.tracking.exception;

import com.furkantufan.courier.tracking.common.exception.BaseException;

import java.io.Serial;

public class StoreNotFoundException extends BaseException {

    @Serial
    private static final long serialVersionUID = -6587964918609261125L;

    public StoreNotFoundException(String message) {
        super(message);
    }
}
