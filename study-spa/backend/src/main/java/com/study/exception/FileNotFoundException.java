package com.study.exception;

/**
 * 잘못된 pk로 파일 찾을 때
 */
public class FileNotFoundException extends RuntimeException{
    public FileNotFoundException() {
    }

    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
