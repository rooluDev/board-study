package com.study.service;

import com.study.dto.CommentDTO;
import com.study.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Comment 서비스
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
    public List<CommentDTO> getComments(Long boardId) {
        return commentMapper.findByBoardId(boardId);
    }

    /**
     * 댓글 생성
     * @param boardId
     * @param comment
     */
    public void createComment(Long boardId, String comment){
        commentMapper.insertComment(boardId, comment);
    }

    /**
     * 댓글 삭제
     * @param boardId
     */
    public void deleteComments(Long boardId){
        commentMapper.deleteByBoardId(boardId);
    }

}
