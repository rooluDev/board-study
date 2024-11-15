package com.study.entity;

import com.study.dto.BoardDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Board Entity
 */
@Getter
@Builder
public class Board {
    private final Long boardId;
    private final Long categoryId;
    private final String title;
    private final Long views;
    private final Timestamp createdAt;
    private final Timestamp editedAt;
    private final String content;
    private final String userName;
    private final String password;

    public BoardDto toBoardDto(){
        return BoardDto.builder()
                .boardId(boardId)
                .categoryId(categoryId)
                .title(title)
                .views(views)
                .createdAt(createdAt)
                .editedAt(editedAt)
                .content(content)
                .userName(userName)
                .build();
    }
}
