
package com.study.exception;

public class UserNameException extends IllegalStateException {
    public UserNameException() {
    }

    public UserNameException(String msg) {
        super(msg);
    }

    public UserNameException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
