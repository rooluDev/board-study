package com.study.DTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
public class BoardDTO {
    private int boardId;
    private int categoryId;
    private String title;
    private int views;
    private Timestamp createdAt;
    private Timestamp editedAt;
    private String content;
    private String userName;
    private String password;
}
