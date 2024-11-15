package com.study.form;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Board 추가시 데이터
 */
@Getter
@Setter
@Builder
public class CreateBoardForm {
    private Long categoryId;
    private String userName;
    private String password;
    private String passwordCheck;
    private String title;
    private String content;
    private MultipartFile[] file;

}
