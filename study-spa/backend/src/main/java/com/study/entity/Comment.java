package com.study.entity;

import com.study.dto.CommentDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Comment Entity
 */
@Getter
@Builder
public class Comment {
    private final Long commentId;
    private final Long boardId;
    private final String comment;
    private final Timestamp createdAt;
    private final Timestamp editedAt;

    /**
     * Entity -> Dto
     * @return
     */
    public CommentDto toCommentDto(){
        return CommentDto.builder()
                .commentId(commentId)
                .comment(comment)
                .createdAt(createdAt)
                .build();
    }
}
