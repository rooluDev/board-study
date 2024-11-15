package com.study.dto;

import lombok.*;

import java.sql.Timestamp;

/**
 * BoardDTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDto {
    private Long boardId;
    private Long categoryId;
    private String title;
    private Long views;
    private Timestamp createdAt;
    private Timestamp editedAt;
    private String content;
    private String userName;
    private String password;
    private String categoryName; // 게시판 - 목록에 필요한 카테고리 이름 변수
    private Long boardIdInFile; // 게시판 - 목록에 필요한 파일 여부를 위한 변수
}