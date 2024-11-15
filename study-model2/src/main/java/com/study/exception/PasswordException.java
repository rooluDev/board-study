
package com.study.exception;

public class PasswordException extends IllegalStateException {
    public PasswordException() {
    }

    public PasswordException(String msg) {
        super(msg);
    }

    public PasswordException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
