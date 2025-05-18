package com.study.service;

import com.study.dto.CommentDto;
import com.study.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Comment 서비스
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    /**
     * 게시물에 있느 댓글 리스트 가져오기
     *
     * @param boardId board PK
     * @return 게시물에 있는 댓글 리스트
     */
    public List<CommentDto> getCommentList(Long boardId) {
        return commentMapper.selectByBoardId(boardId);
    }

    /**
     * 댓글 생성
     *
     * @param comment 저장할 댓글
     */
    public void createComment(CommentDto comment){
        commentMapper.insertComment(comment);
    }

    /**
     * 게시물에 있는 댓글 리스트 삭제
     *
     * @param boardId pk
     */
    public void deleteCommentListByBoardId(Long boardId){
        commentMapper.deleteByBoardId(boardId);
    }

}
