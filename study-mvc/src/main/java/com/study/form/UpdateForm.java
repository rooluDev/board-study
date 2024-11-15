package com.study.form;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * Board 수정시 데이터
 */
@Getter
@Setter
@Builder
public class UpdateForm {
    private String userName;
    private String title;
    private String password;
    private String content;
    private Long boardId;
    private int pageNum;
    private List<Long> deleteFileIds;
}
