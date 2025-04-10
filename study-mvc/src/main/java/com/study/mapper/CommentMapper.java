package com.study.mapper;

import com.study.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Comment DTO Mapper
 */
@Mapper
public interface CommentMapper {

    /**
     * boardId로 댓글 가져오기
     *
     * @param boardId board Pk
     * @return 게시물에 있는 댓글 리스트
     */
    List<CommentDTO> selectByBoardId(Long boardId);

    /**
     * 댓글 추가
     *
     * @param comment 저장 할 댓글
     */
    void insertComment(CommentDTO comment);

    /**
     * board의 댓글 삭제
     *
     * @param boardId board pk
     */
    void deleteByBoardId(Long boardId);
}
