package com.study.exception;

/**
 * Password 영문/숫자/특수문자 포함 4글자 이상 16글자 미만 불충족시
 */
public class PasswordRegexException extends RuntimeException{
    public PasswordRegexException() {
    }

    public PasswordRegexException(String message) {
        super(message);
    }

    public PasswordRegexException(String message, Throwable cause) {
        super(message, cause);
    }
}
