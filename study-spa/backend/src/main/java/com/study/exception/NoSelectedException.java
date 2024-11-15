package com.study.exception;

/**
 * category 선택 안 했을 경우 Exception
 */
public class NoSelectedException extends RuntimeException{
    public NoSelectedException() {
    }

    public NoSelectedException(String message) {
        super(message);
    }

    public NoSelectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
