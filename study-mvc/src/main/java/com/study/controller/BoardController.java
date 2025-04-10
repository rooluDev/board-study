package com.study.controller;

import com.study.condition.SearchCondition;
import com.study.dto.BoardDTO;
import com.study.dto.CategoryDTO;
import com.study.dto.CommentDTO;
import com.study.dto.FileDTO;
import com.study.service.BoardService;
import com.study.service.CategoryService;
import com.study.service.CommentService;
import com.study.service.FileService;
import com.study.validate.BoardValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Board Controller
 */
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CategoryService categoryService;
    private final FileService fileService;
    private final CommentService commentService;

    /**
     * 게시판 - 목록 페이지
     *
     * @param model           Model
     * @param searchCondition 검색조건
     * @return list
     */
    @GetMapping(value = {"/board/list"})
    public String getBoardList(Model model, @ModelAttribute SearchCondition searchCondition) {

        // 필요한 정보들 가져오기
        List<BoardDTO> boardList = boardService.getBoardListByCondition(searchCondition);
        int boardCount = boardService.getBoardCountByCondition(searchCondition);
        List<CategoryDTO> categoryList = categoryService.getCategoryList();

        // 필요한 정보들 설정
        int totalPageNum = (int) Math.ceil((double) boardCount / (double) searchCondition.getPageSize());

        // 정보들 넘겨주기
        model.addAttribute("boardList", boardList);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("totalPageNum", totalPageNum);
        model.addAttribute("boardCount", boardCount);
        model.addAttribute("searchCondition", searchCondition);

        return "list";
    }

    /**
     * 게시판 - 등록 페이지
     *
     * @param model           Model
     * @param searchCondition 검색조건 유지
     * @return post
     */
    @GetMapping(value = {"/board/post"})
    public String postBoard(Model model, @ModelAttribute SearchCondition searchCondition) {

        // 필요한 정보 가져오기
        List<CategoryDTO> categoryList = categoryService.getCategoryList();

        // 정보 넘겨주기
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("searchCondition", searchCondition);

        return "post";
    }

    /**
     * 게시판 - 보기 페이지
     *
     * @param model           Model
     * @param boardId         pathVariable
     * @param searchCondition 검색조건 유지
     * @return view
     */
    @GetMapping(value = {"/board/view/{boardId}"})
    public String viewBoard(Model model, @PathVariable Long boardId, @ModelAttribute SearchCondition searchCondition) {

        // 필요한 정보 가져오기
        BoardDTO board = boardService.getBoard(boardId);
        List<FileDTO> fileList = fileService.getFileListByBoardId(boardId);
        List<CommentDTO> commentList = commentService.getCommentList(boardId);

        // view 증가
        boardService.increaseView(boardId);

        // 정보 넘겨주기
        model.addAttribute("board", board);
        model.addAttribute("fileList", fileList);
        model.addAttribute("commentList", commentList);
        model.addAttribute("searchCondition", searchCondition);

        return "view";
    }

    /**
     * 게시판 - 수정 페이지
     *
     * @param model           Model
     * @param boardId         pathVariable
     * @param searchCondition 검색조건 유지
     * @return edit
     */
    @GetMapping("/board/edit/{boardId}")
    public String editBoard(Model model, @PathVariable Long boardId, @ModelAttribute SearchCondition searchCondition) {

        // 정보 가져오기
        BoardDTO board = boardService.getBoard(boardId);
        List<FileDTO> fileList = fileService.getFileListByBoardId(boardId);

        // 정보 넘겨주기
        model.addAttribute("board", board);
        model.addAttribute("fileList", fileList);
        model.addAttribute("searchCondition", searchCondition);

        return "edit";
    }

    /**
     * 게시판 등록 POST
     *
     * @param board    등록할 게시물
     * @param fileList 등록할 파일들
     * @return list
     */
    @PostMapping(value = {"/board/post"})
    public String postBoard(@ModelAttribute BoardDTO board, @RequestParam(name = "file") List<MultipartFile> fileList) {
        // 유효성 검사
        if (!BoardValidator.validateBoardForPost(board)) {
            return "redirect:/error";
        }


        // board 저장
        Long boardId = boardService.postBoard(board);

        // file 저장
        try {
            fileService.uploadFile(fileList, boardId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/board/list";
    }

    /**
     * 게시물 수정
     *
     * @param board            수정할 게시물
     * @param fileList         등록할 파일
     * @param deleteFileIdList 삭제할 파일 pk 리스트
     * @return list
     */
    @PostMapping(value = {"/board/edit/{boardId}"})
    public String updateBoard(@ModelAttribute BoardDTO board,
                              @PathVariable Long boardId,
                              @RequestParam(name = "newFile", required = false) List<MultipartFile> fileList,
                              @RequestParam(name = "deleteFileIdList", required = false) List<Long> deleteFileIdList) throws IOException {

        if (!BoardValidator.validateBoardForEdit(board)) {
            return "redirect:/board/list";
        }
        // 게시물 수정
        board.setBoardId(boardId);
        boardService.editBoard(board);

        // 파일 삭제
        if (deleteFileIdList != null && !deleteFileIdList.isEmpty()) {
            deleteFileIdList.forEach(fileService::deleteById);
        }

        if (fileList != null && !fileList.isEmpty()) {
            fileService.uploadFile(fileList, board.getBoardId());
        }

        return "redirect:/board/list";
    }

    /**
     * 게시물 삭제
     *
     * @param boardId pathVariable
     * @return redirect /board/list
     */
    @GetMapping(value = {"/board/delete/{boardId}"})
    public String deleteBoard(@PathVariable Long boardId) {
        // 삭제
        commentService.deleteCommentListByBoardId(boardId);
        fileService.deleteFileListByBoardId(boardId);
        boardService.deleteBoard(boardId);

        return "redirect:/board/list";
    }

    /**
     * 비밀번호 확인
     *
     * @param boardId         pk
     * @param enteredPassword 입력한 비밀번호
     * @return ResponseEntity
     */
    @PostMapping(value = {"/board/passwordConfirm"})
    public ResponseEntity confirmPassword(@RequestParam(name = "boardId") Long boardId, @RequestParam(name = "enteredPassword") String enteredPassword) {

        // 비밀번호 불일치
        if (!boardService.findByIdAndPassword(boardId, enteredPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //비밀번호 일치
        return ResponseEntity.ok().build();
    }
}
