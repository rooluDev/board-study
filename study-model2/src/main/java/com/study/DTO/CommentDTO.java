package com.study.DTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class CommentDTO {
    private int commentId;
    private int boardId;
    private String comment;
    private Timestamp createdAt;
    private Timestamp editedAt;
}
