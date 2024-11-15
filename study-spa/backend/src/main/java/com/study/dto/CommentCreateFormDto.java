package com.study.dto;

import lombok.Getter;

@Getter
public class CommentCreateFormDto {
    private Long boardId;
    private String comment;
}
