package com.hyunwoo.sprinkling.model;

import com.hyunwoo.sprinkling.constants.StatusCode;
import com.hyunwoo.sprinkling.exception.MoneySprinklingException;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ApiResponse {
    private boolean result;
    private String description;
    private ResponseData data;

    public ApiResponse(StatusCode code){
        result = code.getCode() == 1200;
        description = code.getDescription();
    }

    public ApiResponse(MoneySprinklingException exp){
        result = exp.getResultCode() == 1200;
        description = exp.getDescription();
    }

    public ApiResponse(ResponseData data){
        result = true;
        description = "Success";
        this.data = data;
    }

    public static ApiResponse of(MoneySprinklingException e){
        return new ApiResponse(e);
    }

    public static ApiResponse of(Exception e){
        return new ApiResponse(StatusCode.E500);
    }

}
