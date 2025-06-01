package com.study.controller;

import com.study.condition.SearchCondition;
import com.study.dto.*;
import com.study.service.BoardService;
import com.study.service.CommentService;
import com.study.service.FileService;
import com.study.validate.BoardValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * board rest api controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;
    private final CommentService commentService;


    /**
     * 게시판 리스트 페이지에 필요한 데이터
     *
     * @param searchCondition 검색조건
     * @return {
     * boardList : []
     * totalPageNum : 0
     * }
     */
    @GetMapping("/boards")
    public ResponseEntity<Map<String, Object>> getBoardList(@ModelAttribute SearchCondition searchCondition) {

        List<BoardDto> boardList = boardService.getBoardListByCondition(searchCondition);
        int boardCount = boardService.getBoardCountByCondition(searchCondition);

        Map<String, Object> response = new HashMap<>();
        response.put("boardList", boardList);
        response.put("boardCount", boardCount);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 게시판 보기 페이지 및 수정 페이지에 필요한 데이터
     *
     * @param boardId pk
     * @return {
     * board :{},
     * fileList : [],
     * commentList : []
     * }
     */
    @GetMapping("/board/{boardId}")
    public ResponseEntity<Map<String, Object>> getBoard(@PathVariable(name = "boardId") Long boardId) {
        // 필요한 정보 요청
        BoardDto boardDTO = boardService.getBoard(boardId);
        if (boardDTO == null) {
            throw new NoSuchElementException();
        }

        List<FileDto> fileList = fileService.getFileListByBoardId(boardId);
        List<CommentDto> commentList = commentService.getCommentList(boardId);

        Map<String, Object> response = new HashMap<>();
        response.put("board", boardDTO);
        response.put("fileList", fileList);
        response.put("commentList", commentList);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 게시물 추가
     *
     * @param boardDto 추가할 게시물 데이터
     * @param fileList 추가할 파일 리스트
     * @return null
     */
    @PostMapping("/board")
    public ResponseEntity postBoard(@ModelAttribute BoardDto boardDto,
                                    @RequestPart(name = "file", required = false) List<MultipartFile> fileList) throws IOException {


        // 유효성 검사
        if (!BoardValidator.validateBoardForPost(boardDto)) {
            throw new IllegalStateException();
        }

        // DB에 board 저장
        Long boardId = boardService.postBoard(boardDto);

        // File 저장
        if (fileList != null && !fileList.isEmpty()) {
            fileService.addFile(fileList, boardId);
        }

        return ResponseEntity.ok().body(null);
    }

    /**
     * 게시물 수정
     *
     * @param boardId          pk
     * @param boardDto         수정할 게시물 데이터
     * @param fileList         추가할 파일
     * @param deleteFileIdList 삭제할 파일의 pk 리스트
     * @return null
     */
    @PutMapping("/board/{boardId}")
    public ResponseEntity updateBoard(@PathVariable(name = "boardId") Long boardId,
                                      @ModelAttribute BoardDto boardDto,
                                      @RequestPart(name = "file", required = false) List<MultipartFile> fileList,
                                      @RequestPart(name = "deleteFileIdList", required = false) List<Long> deleteFileIdList) throws IOException {

        if (!BoardValidator.validateBoardForEdit(boardDto)) {
            throw new IllegalStateException();
        }
        boardDto.setBoardId(boardId);
        boardService.editBoard(boardDto);

        if (deleteFileIdList != null && !deleteFileIdList.isEmpty()) {
            deleteFileIdList.forEach(fileService::deleteById);
        }

        if (fileList != null && !fileList.isEmpty()) {
            fileService.addFile(fileList, boardId);
        }

        return ResponseEntity.ok().body(null);
    }

    /**
     * 게시물 조회수 증가
     *
     * @param boardId pk
     * @return null
     */
    @PatchMapping("/board/{boardId}/increase-view")
    public ResponseEntity increaseView(@PathVariable(name = "boardId") Long boardId) {

        boardService.increaseView(boardId);

        return ResponseEntity.ok().body(null);
    }

    /**
     * 게시물 삭제
     *
     * @param boardId pathVariable
     * @return null
     */
    @DeleteMapping("/board/{boardId}")
    public ResponseEntity deleteBoard(@PathVariable Long boardId) {
        commentService.deleteCommentListByBoardId(boardId);
        fileService.deleteFileListByBoardId(boardId);
        boardService.deleteBoard(boardId);

        return ResponseEntity.ok().body(null);
    }

    /**
     * 비밀번호 확인
     *
     * @param boardId         pk
     * @param enteredPassword 입력한 비밀번호
     * @return ResponseEntity
     */
    @PostMapping(value = {"/board/password-check"})
    public ResponseEntity checkPassword(@RequestParam(name = "boardId") Long boardId, @RequestParam(name = "enteredPassword") String enteredPassword) throws IllegalAccessException {

        // 비밀번호 불일치
        if (!boardService.findByIdAndPassword(boardId, enteredPassword)) {
            throw new IllegalAccessException("비밀번호가 일치하지 않음");
        }

        //비밀번호 일치
        return ResponseEntity.ok().build();
    }
}
