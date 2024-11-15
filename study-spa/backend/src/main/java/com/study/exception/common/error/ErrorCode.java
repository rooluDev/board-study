package com.study.exception.common.error;

import lombok.Getter;

/**
 * Error Code
 */
@Getter
public enum ErrorCode {
    // @RequestBody 데이터 미 존재
    REQUEST_BODY_MISSING_ERROR(404, "A001", "Required request body is missing"),
    // Request Parameter 로 데이터가 전달되지 않을 경우
    MISSING_REQUEST_PARAMETER_ERROR(404, "A002", "Missing Servlet RequestParameter Exception"),
    // SQL Exception 전체 처리
    SQL_ERROR(500,"A003","SQL_Exception"),
    // IO Exception 전체 처리
    IO_ERROR(500, "A004", "I/O Exception"),
    // NULL Point Exception 발생
    NULL_POINT_ERROR(500, "A005", "Null Point Exception"),
    // @RequestBody 및 @RequestParam, @PathVariable 값이 유효하지 않음
    NOT_VALID_ERROR(400, "A006", "handle Validation Exception"),
    // 서버가 처리 할 방법을 모르는 경우 발생
    INTERNAL_SERVER_ERROR(500, "A007", "Internal Server Error Exception"),
    // 존재하지 않은 pk로 board 조회시
    BOARD_NOT_FOUND_ERROR(404, "A008", "Board Not Found Exception"),
    // 존재하지 않은 pk로 category 조회시
    CATEGORY_NOT_FOUND_ERROR(404,"A009","Category Not Found Exception"),
    // // 존재하지 않은 pk로 file 조회시
    FILE_NOT_FOUND_ERROR(404,"A010","File Not Found Exception"),
    // board input data가 정책상의 길이와 일치하지 않음
    ILLEGAL_LENGTH_ERROR(403,"A011","Illegal Length Exception"),
    // 카테고리 선택이 안되어있을 때
    NO_SELECTED_ERROR(404,"A012","No Category Selected Exception"),
    // password, passwordCheck 값이 일치하지 않을 때
    PASSWORD_INCORRECT_ERROR(400,"A013","Password Incorrect Exception"),
    // password가 정책상의 규정과 맞지 않을 때
    PASSWORD_REGEX_ERROR(400,"A014","Password Regex Exception");



    private final int status;
    private final String errorCode;
    private final String message;

    ErrorCode(final int status, final String errorCode, final String message) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
    }
}
