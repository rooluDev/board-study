package com.study.mapper;

import com.study.condition.SearchCondition;
import com.study.dto.BoardDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * BoardDto mapper
 */
@Mapper
public interface BoardMapper {

    /**
     * 검색조건에 따른 BoardList 가져오기
     *
     * @param searchCondition 검색조건
     * @return BoardList
     */
    List<BoardDto> selectBoardListByCondition(SearchCondition searchCondition);

    /**
     * 검색조건에 따른 Board rowcount 가져오기
     *
     * @param searchCondition 검색조건
     * @return board count
     */
    int selectBoardCountByCondition(SearchCondition searchCondition);

    /**
     * 단일 게시물 가져오기
     *
     * @param boardId pk
     * @return 단일 게시물
     */
    BoardDto selectById(Long boardId);

    /**
     * board 추가
     *
     * @param board 추가할 게시물
     * @return 추가 후 생성된 pk
     */
    int insertBoard(BoardDto board);

    /**
     * board 데이터 update
     *
     * @param board 수정할 게시물 데이터
     */
    void updateBoard(BoardDto board);

    /**
     * view 1 증가
     *
     * @param boardId pk
     */
    void updateView(Long boardId);

    /**
     * pk로 board 삭제
     *
     * @param boardId pk
     */
    void deleteById(Long boardId);

    /**
     * pk와 입력한 비밀번호와 일치하는 게시물의 수 가져오기
     *
     * @param boardId         pk
     * @param enteredPassword 입력한 pw
     * @return 일치하는 게시물의 수
     */
    int selectByIdAndPassword(@Param("boardId") Long boardId, @Param("enteredPassword") String enteredPassword);

}


