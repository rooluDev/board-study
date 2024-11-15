package com.study.service;

import com.study.dto.BoardDTO;
import com.study.condition.BoardSelectCondition;
import com.study.mapper.BoardMapper;
import com.study.returnType.BoardListAndCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 보드 서비스
 */
@Service
public class BoardService {
    private final BoardMapper boardMapper;

    @Autowired
    public BoardService(BoardMapper boardMapper){
        this.boardMapper = boardMapper;
    }

    /**
     * 검색
     * @param boardSelectCondition
     * @return
     */
    public BoardListAndCount search(BoardSelectCondition boardSelectCondition){
        BoardListAndCount boardListAndCount = BoardListAndCount.builder()
                .boardList(boardMapper.getBoardList(boardSelectCondition))
                .rowCount(boardMapper.getBoardCount(boardSelectCondition))
                .build();

        return boardListAndCount;
    }

    /**
     * 추가
     * @param board
     * @return
     */
    public void addBoard(BoardDTO board){
        boardMapper.createBoard(board);
    }

    /**
     * boardId로 BoardDTO 찾기
     * @param boardId
     * @return
     */
    public BoardDTO findBoard(long boardId){
        return boardMapper.findById(boardId);
    }

    /**
     * board 수정
     * @param board
     */
    public void modifyBoard(BoardDTO board){
        boardMapper.updateBoard(board);
    }

    /**
     * password 찾기
     * @param boardId
     * @return
     */
    public String findPassword(Long boardId){
        return boardMapper.selectPassword(boardId);
    }

    /**
     * 조회수 증가
     * @param boardId
     */
    public void increaseView(Long boardId){
        boardMapper.updateView(boardId);
    }

    /**
     * 게시물 삭제
     * @param boardId
     */
    public void deleteBoard(Long boardId){
        boardMapper.deleteById(boardId);
    }
}
