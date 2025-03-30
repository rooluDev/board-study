package com.study.service;

import com.study.condition.SearchCondition;
import com.study.dto.Board;
import com.study.repository.BoardRepository;

import java.util.List;

/**
 * 게시판 서비스 계층
 */
public class BoardService {

    private final BoardRepository boardRepository = BoardRepository.getInstance();

    /**
     * 검색조건에 따른 게시물 리스트 반환
     *
     * @return 게시물 리스트
     */
    public List<Board> getBoardListForMain(SearchCondition searchCondition) throws Exception {
        return boardRepository.selectBoardList(searchCondition);
    }

    /**
     * 페이지네이션에 상관없는 검색조건에 맞는 모든 게시물
     *
     * @param searchCondition 검색조건
     * @return 게시물 수
     * @throws Exception
     */
    public int getBoardCountForMain(SearchCondition searchCondition) throws Exception {
        return boardRepository.selectRowCountForBoardList(searchCondition);
    }

    /**
     * 특정 게시물 반환
     *
     * @return 게시물
     */
    public Board getBoard(int boardId) throws Exception {
        return boardRepository.selectBoardById(boardId);
    }

    /**
     * 특정 게시물 조회수 1 증가
     *
     * @param boardId pk
     * @throws Exception
     */
    public void plusViews(int boardId) throws Exception {
        boardRepository.plusViews(boardId);
    }

    /**
     * 게시물 추가
     *
     * @param board 추가할 게시물
     * @return PK
     * @throws Exception
     */
    public int addBoard(Board board) throws Exception {
        return boardRepository.insertBoard(board);
    }

    /**
     * 게시물 삭제
     *
     * @param boardId PK
     * @throws Exception
     */
    public void deleteBoardById(int boardId) throws Exception {
        boardRepository.deleteById(boardId);
    }

    /**
     * 게시물 수정
     *
     * @param board 수정할 데이터
     * @throws Exception
     */
    public void editBoard(Board board) throws Exception {
        boardRepository.updateBoard(board);
    }

    /**
     * 특정 게시물의 비밀번호가 일치하는지 확인
     *
     * @param boardId pk
     * @param password input Passsword
     * @return 존재유무
     * @throws Exception
     */
    public boolean findBoardByPasswordAndId(int boardId, String password) throws Exception {
        return boardRepository.selectBoardByPassword(boardId, password) == 1;
    }

}
