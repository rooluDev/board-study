package com.study.dto;

import lombok.Getter;

/**
 * 게시물 수정 form data
 */
@Getter
public class BoardUpdateFormDto {
    private String userName;
    private String title;
    private String content;
    private String passwordCheck;
}
