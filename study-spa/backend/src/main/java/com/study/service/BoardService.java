package com.study.service;

import com.study.condition.BoardSelectCondition;
import com.study.dto.BoardDto;
import com.study.exception.BoardNotFoundException;
import com.study.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 게시물 관련 비지니스 로직
 */
@Service
public class BoardService {
    private final BoardMapper boardMapper;

    @Autowired
    public BoardService(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    /**
     * 게시물 with Category Name 찾기
     *
     * @param boardId
     * @return
     */
    public BoardDto findBoard(long boardId) throws BoardNotFoundException {
        return boardMapper.findById(boardId).orElseThrow(() -> new BoardNotFoundException());
    }

    /**
     * 검색조건에 따라 게시물 with CategoryName List 찾기
     *
     * @param boardSelectCondition
     * @return
     */
    public List<BoardDto> getBoardList(BoardSelectCondition boardSelectCondition) {
        List<BoardDto> boardCategoryFileDtoList = boardMapper.getBoardList(boardSelectCondition);
        return boardCategoryFileDtoList;
    }

    /**
     * 검색조건에 따라 게시물 총 수
     *
     * @param boardSelectCondition
     * @return
     */
    public int getBoardCount(BoardSelectCondition boardSelectCondition) {
        return boardMapper.getBoardCount(boardSelectCondition);
    }

    /**
     * 게시물 추가
     *
     * @param board
     * @return
     */
    public int addBoard(BoardDto board) {
        return boardMapper.createBoard(board);
    }

    /**
     * 게시물 조회수 증가
     *
     * @param boardId
     */
    public int increaseView(Long boardId) {
        return boardMapper.updateView(boardId);
    }

    /**
     * 게시물 삭제
     *
     * @param boardId
     */
    public int deleteBoardById(Long boardId) {
        return boardMapper.deleteById(boardId);
    }

    /**
     * 게시물 수정
     *
     * @param board
     */
    public int updateBoard(BoardDto board) {
        return boardMapper.updateBoard(board);
    }
}
