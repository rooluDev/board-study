package com.study.exception.common.success;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String successCode;
    private String successMessage;
    private T body;


    public ApiResponse(final T body, final SuccessCode successCode){
        this.body = body;
        this.status = successCode.getStatus();
        this.successCode = successCode.getSuccessCode();
        this.successMessage = successCode.getMessage();
    }
}
