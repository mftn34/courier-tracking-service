package com.furkantufan.courier.tracking.common.exception;

import java.io.Serial;
import java.io.Serializable;

public abstract class BaseException extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 2463738663161039829L;

    protected BaseException() {
        super();
    }

    protected BaseException(String message) {
        super(message);
    }

    protected BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    protected BaseException(Throwable cause) {
        super(cause);
    }
}
