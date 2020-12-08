package com.hyunwoo.sprinkling.exception;

import com.hyunwoo.sprinkling.model.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class MoneySprinklingControllerHandler {

    @ExceptionHandler(MoneySprinklingException.class)
    public ResponseEntity<ApiResponse> handleMoneySprinklingException(MoneySprinklingException e) {
        log.error("[MoneySprinklingControllerHandler][MoneySprinklingException] {}", e.getDescription());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.of(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleUnauthorized(Exception e) {
        log.error("[MoneySprinklingControllerHandler][Exception] {}", e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.of(e));
    }
}
