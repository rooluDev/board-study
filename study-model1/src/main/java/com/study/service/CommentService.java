package com.study.service;

import com.study.dto.Comment;
import com.study.repository.CommentRepository;

import java.util.List;

/**
 * Comment Service
 */
public class CommentService {

    CommentRepository commentRepository = CommentRepository.getInstance();

    /**
     * 게시물에 등록되어있는 댓글
     *
     * @param boardId 게시물 PK
     * @return 댓글리스트
     */
    public List<Comment> getCommentListById(int boardId) throws Exception {
        return commentRepository.selectCommentListByBoardId(boardId);
    }

    /**
     * 댓글 등록
     *
     * @param comment 저장할 댓글
     * @throws Exception
     */
    public void addComment(Comment comment) throws Exception {
        commentRepository.insertComment(comment);
    }

    /**
     * 게시물에 등록된 모든 댓글 삭제
     *
     * @param boardId boardPk
     * @throws Exception
     */
    public void deleteCommentsByBoardId(int boardId) throws Exception {
        commentRepository.deleteCommentsByBoardId(boardId);
    }
}
