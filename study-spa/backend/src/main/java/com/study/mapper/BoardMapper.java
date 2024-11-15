package com.study.mapper;

import com.study.condition.BoardSelectCondition;
import com.study.dto.BoardDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * Board mapper
 */
@Mapper
public interface BoardMapper {
    /**
     * 검색조건과 페이지네이션에 따라 목록에 보여줄 데이터 검색
     * @param boardSelectCondition
     * @return
     */
    List<BoardDto> getBoardList(BoardSelectCondition boardSelectCondition);

    /**
     * 검색조건에 따른 전체 Board rowcount 가져오기
     * @param boardSelectCondition
     * @return
     */
    int getBoardCount(BoardSelectCondition boardSelectCondition);

    /**
     * pk로 board 데이터와 카테고리 이름 가져오기
     * @param boardId
     * @return
     */
    Optional<BoardDto> findById(Long boardId);

    /**
     * board 추가
     * @param boardDTO
     */
    int createBoard(BoardDto boardDTO);

    /**
     * view 1 증가
     * @param boardId
     */
    int updateView(Long boardId);

    /**
     * pk로 board 삭제
     * @param boardId
     */
    int deleteById(Long boardId);

    /**
     * board update
     * @param board
     */
    int updateBoard(BoardDto board);
}
