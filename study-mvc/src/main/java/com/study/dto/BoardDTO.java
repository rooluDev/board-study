package com.study.dto;

import lombok.*;

import java.sql.Timestamp;


/**
 * BoardDTO
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
    private Long boardId;
    private Long categoryId;
    private String title;
    private Long views;
    private Timestamp createdAt;
    private Timestamp editedAt;
    private String content;
    private String userName;
    private String password;
    private String passwordRe;
    // Join 연산 시 메인 페이지 카테고리를 위한 추가 멤버변수
    private String categoryName;
    // Join 연산 시 메인 페이지 파일 여부를 위한 추가 멤버변수
    private int fileId;
}