package com.study.exception;

public class TitleException extends IllegalStateException {
    public TitleException() {
    }

    public TitleException(String msg) {
        super(msg);
    }

    public TitleException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
