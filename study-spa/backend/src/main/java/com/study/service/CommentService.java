package com.study.service;

import com.study.dto.CommentCreateFormDto;
import com.study.dto.CommentDto;
import com.study.entity.Comment;
import com.study.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 댓글 관련 비지니스 로직
 */
@Service
public class CommentService {
    private final CommentMapper commentMapper;

    @Autowired
    public CommentService(CommentMapper commentMapper){
        this.commentMapper = commentMapper;
    }

    /**
     * 댓글 가져오기
     * @param boardId
     * @return
     */
    public List<CommentDto> getComments(Long boardId){
        // Entity -> DTO
        List<CommentDto> commentDtoList = commentMapper.findByBoardId(boardId).stream()
                .map(entity -> entity.toCommentDto())
                .collect(Collectors.toList());
        return commentDtoList;
    }

    /**
     * 댓글 등록
     * @param commentCreateFormDto
     */
    public int addComment(CommentCreateFormDto commentCreateFormDto){
        return commentMapper.createComment(commentCreateFormDto);
    }

    /**
     * 댓글 모두 삭제
     */
    public int deleteByBoardId(Long boardId){
        return commentMapper.deleteByBoardId(boardId);
    }
}
