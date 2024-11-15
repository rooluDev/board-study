package com.study.exception.common.error;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Error 응답 Class
 */
@Getter
@NoArgsConstructor
public class ErrorResponse {
    private int status;
    private String errorCode;
    private String resultMessage;

    @Builder
    protected ErrorResponse(final ErrorCode errorCode){
        this.status = errorCode.getStatus();
        this.errorCode = errorCode.getErrorCode();
        this.resultMessage = errorCode.getMessage();
    }

    public static ErrorResponse of(final ErrorCode code){
        return new ErrorResponse(code);
    }
}
