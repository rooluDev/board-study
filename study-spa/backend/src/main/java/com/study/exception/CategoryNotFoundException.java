package com.study.exception;

/**
 * 잘못된 category pk로 category를 select할 때
 */
public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException() {
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
