package com.study.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


/**
 * CommentDTO
 */
@Getter
@Setter
@Builder
public class CommentDto {
    private Long commentId;
    private String comment;
    private Timestamp createdAt;
}
