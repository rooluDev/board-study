
package com.study.exception;

public class ContentException extends IllegalStateException {
    public ContentException() {
    }

    public ContentException(String msg) {
        super(msg);
    }

    public ContentException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
