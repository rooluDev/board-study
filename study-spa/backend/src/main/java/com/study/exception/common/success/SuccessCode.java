package com.study.exception.common.success;

import lombok.Getter;

@Getter
public enum SuccessCode {
    SELECT_SUCCESS(200,"200","SELECT SUCCESS"),
    DELETE_SUCCESS(200,"200","DELETE SUCCESS"),
    INSERT_SUCCESS(200,"201","INSERT SUCCESS"),
    UPDATE_SUCCESS(201,"201","UPDATE_SUCCESS"),
    VERIFICATION_SUCCESS(200,"200","VERIFICATION_SUCCESS"),;

    private final int status;
    private final String successCode;
    private final String message;

    SuccessCode(final int status,final String successCode,final String message){
        this.status = status;
        this.successCode = successCode;
        this.message = message;
    }
}
