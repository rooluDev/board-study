package com.study.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * 댓글 Dto
 */
@Getter
@Setter
@Builder
public class Comment {
    private int commentId;
    private int boardId;
    private String comment;
    private Timestamp createdAt;
    private Timestamp editedAd;
}
