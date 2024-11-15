package com.study.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class BoardCreateFormDto {
    private MultipartFile[] files;
    private Long categoryId;
    private String userName;
    private String password;
    private String passwordCheck;
    private String title;
    private String content;
}
