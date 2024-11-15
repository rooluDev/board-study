package com.study.exception.common;

import com.study.exception.*;
import com.study.exception.common.error.ErrorCode;
import com.study.exception.common.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.sql.SQLException;

/**
 * RestController에서 발생하는 Exception 처리하는 GlobalExceptionHandler
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * SQLException 처리 Handler
     * @return
     */
    @ExceptionHandler(SQLException.class)
    protected  ResponseEntity<ErrorResponse> handleSQLException(){
        log.error("SQL Exception");
        final ErrorResponse response = ErrorResponse.of(ErrorCode.SQL_ERROR);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * IOException 처리 핸들러
     * @return
     */
    @ExceptionHandler(IOException.class)
    protected  ResponseEntity<ErrorResponse> handleIOException(){
        log.error("IOException");
        final ErrorResponse response = ErrorResponse.of(ErrorCode.IO_ERROR);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * @requestBody @requestParam @ModelAttribute에 유효하지 않은 값이 넘어올 때
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected  ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(){
        log.error("MethodArgumentNotValidException");
        final ErrorResponse response = ErrorResponse.of(ErrorCode.MISSING_REQUEST_PARAMETER_ERROR);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * @PathVaiable에 유효하지 않은 값이 넘어올 때
     * @return
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected  ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(){
        log.error("MethodArgumentTypeMismatchException");
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_VALID_ERROR);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * NullPointerException처리, @requsetBody에 변수 값이 넘어오지 않았을 때
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    protected  ResponseEntity<ErrorResponse> handleNullPointerException(){
        log.error("NullPointerException");
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NULL_POINT_ERROR);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 존재하지 않은 pk로 board select해서 빈값 넘어올 때
     * @return
     */
    @ExceptionHandler(BoardNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleBoardNotFoundException(){
        log.error("BoardNotFoundException");
        final ErrorResponse response = ErrorResponse.of(ErrorCode.BOARD_NOT_FOUND_ERROR);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 존재하지 않은 pk로 category select해서 빈값 넘어올 때
     * @return
     */
    @ExceptionHandler(CategoryNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleCategoryNotFoundException(){
        log.error("CategoryNotFoundException");
        final ErrorResponse response = ErrorResponse.of(ErrorCode.CATEGORY_NOT_FOUND_ERROR);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 존재하지 않은 pk로 file select해서 빈값 넘어올 때
     * @return
     */
    @ExceptionHandler(FileNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleFileNotFoundException(){
        log.error("FileNotFoundException");
        final ErrorResponse response = ErrorResponse.of(ErrorCode.FILE_NOT_FOUND_ERROR);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * board Input data 중 userName, content, title 의 길이가 정책의 규정에 맞지 않을 때
     * @return
     */
    @ExceptionHandler(IllegalLengthException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalLengthException(){
        log.error("IllegalLengthException");
        final ErrorResponse response = ErrorResponse.of(ErrorCode.ILLEGAL_LENGTH_ERROR);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 필수 선택 영역인 category가 선택 안되어서 넘어올 때
     * @return
     */
    @ExceptionHandler(NoSelectedException.class)
    protected ResponseEntity<ErrorResponse> handleNoSelectedException(){
        log.error("NoSelectedException");
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NO_SELECTED_ERROR);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * password와 passwordCheck가 일치하지 않을 때
     * @return
     */
    @ExceptionHandler(PasswordIncorrectException.class)
    protected ResponseEntity<ErrorResponse> handlePasswordIncorrectException(){
        log.error("PasswordIncorrectException");
        final ErrorResponse response = ErrorResponse.of(ErrorCode.PASSWORD_INCORRECT_ERROR);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * password가 정책 규정의 만족하지 못할 때
     * @return
     */
    @ExceptionHandler(PasswordRegexException.class)
    protected ResponseEntity<ErrorResponse> handlePasswordRegexException(){
        log.error("PasswordRegexException");
        final ErrorResponse response = ErrorResponse.of(ErrorCode.PASSWORD_REGEX_ERROR);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 예상 못한  Exception이 발생 시
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(){
        log.error("Exception");
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
