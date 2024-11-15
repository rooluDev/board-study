package com.study.exception;

public class CategoryException extends IllegalStateException {
    public CategoryException() {
    }

    public CategoryException(String msg) {
        super(msg);
    }

    public CategoryException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
