package com.study.mapper;

import com.study.dto.BoardDTO;
import com.study.condition.BoardSelectCondition;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * BoardDTO mapper
 */
@Mapper
public interface BoardMapper {

    /**
     * 검색조건에 따른 BoardList 가져오기
     * @param boardSelectCondition
     * @return
     */
    List<BoardDTO> getBoardList(BoardSelectCondition boardSelectCondition);

    /**
     * 검색조건에 따른 Board rowcount 가져오기
     * @param boardSelectCondition
     * @return
     */
    int getBoardCount(BoardSelectCondition boardSelectCondition);

    /**
     * board 추가
     * @param board
     * @return
     */
    void createBoard(BoardDTO board);

    /**
     * pk로 board 데이터 가져오기
     * @param boardId
     * @return
     */
    BoardDTO findById(Long boardId);

    /**
     * board 데이터 update
     * @param board
     */
    void updateBoard(BoardDTO board);

    /**
     * password 가져오기
     * @param boardId
     * @return
     */
    String selectPassword(Long boardId);

    /**
     * view 1 증가
     * @param boardId
     */
    void updateView(Long boardId);

    /**
     * pk로 board 삭제
     * @param boardId
     */
    void deleteById(Long boardId);

}


