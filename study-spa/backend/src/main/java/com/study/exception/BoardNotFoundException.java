package com.study.exception;

/**
 * DB에서 pk로 Board를 select할 때 board가 없을 때
 */
public class BoardNotFoundException extends RuntimeException{
    public BoardNotFoundException() {
    }

    public BoardNotFoundException(String message) {
        super(message);
    }

    public BoardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
