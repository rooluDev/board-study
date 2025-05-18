package com.study.service;

import com.study.condition.SearchCondition;
import com.study.dto.BoardDto;
import com.study.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 보드 서비스
 */
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;

    /**
     * 조건에 맞는 게시물 리스트
     *
     * @param searchCondition 검색조건
     * @return 게시물 리스트
     */
    public List<BoardDto> getBoardListByCondition(SearchCondition searchCondition) {
        return boardMapper.selectBoardListByCondition(searchCondition);
    }

    /**
     * 조건에 맞는 게시물 수
     *
     * @param searchCondition 검색조건
     * @return 게시물 수
     */
    public int getBoardCountByCondition(SearchCondition searchCondition) {
        return boardMapper.selectBoardCountByCondition(searchCondition);
    }

    /**
     * boardId로 BoardDto 찾기
     *
     * @param boardId pk
     * @return 게시물
     */
    public BoardDto getBoard(Long boardId) {
        return boardMapper.selectById(boardId);
    }

    /**
     * 추가
     *
     * @param board 추가할 게시물 데이터
     */
    public Long postBoard(BoardDto board) {
        boardMapper.insertBoard(board);
        return board.getBoardId();
    }

    /**
     * board 수정
     *
     * @param board 수정할 게시물 데이터
     */
    public void editBoard(BoardDto board) {
        boardMapper.updateBoard(board);
    }

    /**
     * boardId와 입력한 pw와 일치하는 지 확인
     *
     * @param boardId         pk
     * @param enteredPassword 입력한 비밀번호
     * @return count
     */
    public boolean findByIdAndPassword(Long boardId, String enteredPassword) {
        return boardMapper.selectByIdAndPassword(boardId, enteredPassword) == 1;
    }

    /**
     * 조회수 증가
     *
     * @param boardId pk
     */
    public void increaseView(Long boardId) {
        boardMapper.updateView(boardId);
    }

    /**
     * 게시물 삭제
     *
     * @param boardId pk
     */
    public void deleteBoard(Long boardId) {
        boardMapper.deleteById(boardId);
    }
}
