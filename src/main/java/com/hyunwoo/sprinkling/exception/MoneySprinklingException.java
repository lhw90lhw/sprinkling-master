package com.hyunwoo.sprinkling.exception;

import com.hyunwoo.sprinkling.constants.StatusCode;
import lombok.Getter;

@Getter
public class MoneySprinklingException extends RuntimeException {
    private String description;
    private int resultCode;
    
    public MoneySprinklingException(StatusCode statusCode){
        this.resultCode = statusCode.getCode();
        this.description = statusCode.getDescription();
    }
}