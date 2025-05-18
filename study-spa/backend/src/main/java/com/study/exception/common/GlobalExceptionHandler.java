package com.study.exception.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * RestController에서 발생하는 Exception 처리하는 GlobalExceptionHandler
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * IllegalStateException 처리 Handler
     *
     * @return BAD_REQUEST BAD_REQUEST
     */
    protected ResponseEntity handleIllegalStateException(IllegalStateException e){
        log.error("IllegalStateException occurred. message={}", e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * NoSuchElementException 처리 Handler
     *
     * @return BAD_REQUEST
     */
    protected ResponseEntity handleNoSuchElementException(NoSuchElementException e){
        log.error("NoSuchElementException occurred. message={}", e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    /**
     * BadCredentialsException 처리 Handler
     *
     * @return UNAUTHORIZED
     */
    protected ResponseEntity handleBadCredentialsException(BadCredentialsException e){
        log.error("BadCredentialsException occurred. message={}", e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    /**
     * SQLException 처리 Handler
     *
     * @return INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(SQLException.class)
    protected ResponseEntity handleSQLException(SQLException e) {
        log.error("SQLException occurred. message={}", e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * IOException 처리 핸들러
     *
     * @return INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(IOException.class)
    protected ResponseEntity handleIOException(IOException e) {
        log.error("IOException occurred. message={}", e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * requestBody @requestParam @ModelAttribute에 유효하지 않은 값이 넘어올 때
     *
     * @return INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException occurred. message={}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * PathVaiable에 유효하지 않은 값이 넘어올 때
     *
     * @return INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("MethodArgumentTypeMismatchException occurred. message={}", e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * NullPointerException처리, @requsetBody에 변수 값이 넘어오지 않았을 때
     *
     * @return INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity handleNullPointerException(NullPointerException e) {
        log.error("NullPointerException occurred. message={}", e.getMessage(), e);


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 예상 못한  Exception이 발생 시
     *
     * @return INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity handleException(Exception e) {
        log.error("Exception occurred. message={}", e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
