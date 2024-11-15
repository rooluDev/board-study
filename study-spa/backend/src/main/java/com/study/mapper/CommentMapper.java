package com.study.mapper;

import com.study.dto.CommentCreateFormDto;
import com.study.dto.CommentDto;
import com.study.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    /**
     * boardId로 댓글 가져오기
     * @param boardId
     * @return
     */
    List<Comment> findByBoardId(Long boardId);

    /**
     * 댓글 등록
     *
     * @param commentCreateFormDto
     */
    int createComment(CommentCreateFormDto commentCreateFormDto);

    /**
     * boardId가 일치하는 댓글 삭제
     * @param boardId
     */
    int deleteByBoardId(Long boardId);

}
