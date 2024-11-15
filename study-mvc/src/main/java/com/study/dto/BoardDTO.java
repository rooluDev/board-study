package com.study.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;


/**
 * BoardDTO
 */
@Getter
@Setter
@Builder
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
}