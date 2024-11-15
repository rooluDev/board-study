package com.study.form;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


/**
 * Comment 추가시 데이터
 */
@Getter
@Setter
@Builder
public class CommentAddForm {
    private String comment;
    private Long boardId;
    private int pageNum;
}
