package com.study.exception;

/**
 * Input data가 최소 글자 및 최대 글자 조건 불충족시
 */
public class IllegalLengthException extends RuntimeException{
    public IllegalLengthException() {
    }

    public IllegalLengthException(String message) {
        super(message);
    }

    public IllegalLengthException(String message, Throwable cause) {
        super(message, cause);
    }
}
