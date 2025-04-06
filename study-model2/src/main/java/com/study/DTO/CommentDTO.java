package com.study.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Comment Dto
 */
@Getter
@Setter
@Builder
public class CommentDTO {
    private int commentId;
    private int boardId;
    private String comment;
    private Timestamp createdAt;
    private Timestamp editedAt;
}
