package com.furkantufan.courier.tracking.common.exception;

import com.furkantufan.courier.tracking.common.config.i18n.MessageSourceConfiguration;
import com.furkantufan.courier.tracking.exception.CourierException;
import com.furkantufan.courier.tracking.exception.StoreNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<ExceptionDetailDtoHolder> handleInternalServerException(Exception ex) {
        log.error(ex.getMessage(), ex);
        var message = MessageSourceConfiguration.getMessage("internal.server.error", ex.getMessage());
        var holder = ExceptionDetailDtoHolder.builder().message(message).build();
        return new ResponseEntity<>(holder, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(StoreNotFoundException.class)
    public ResponseEntity<Object> handleStoreNotFoundException(StoreNotFoundException e) {
        log.error("handleStoreNotFoundException Ex;", e);
        var holder = ExceptionDetailDtoHolder.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(holder, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CourierException.class)
    public ResponseEntity<Object> handleCourierException(CourierException e) {
        log.error("handleStoreNotFoundException Ex;", e);
        var holder = ExceptionDetailDtoHolder.builder().message(e.getMessage()).build();
        return new ResponseEntity<>(holder, HttpStatus.NOT_FOUND);
    }
}