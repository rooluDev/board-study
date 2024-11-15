package com.study.dto;

import lombok.Getter;

/**
 * 비밀번호 확인 form data
 */
@Getter
public class PasswordCheckDto {
    private Long boardId;
    private String password;
}
