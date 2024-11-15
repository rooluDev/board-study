package com.study.controller;

import com.study.dto.BoardDTO;
import com.study.dto.CategoryDTO;
import com.study.dto.CommentDTO;
import com.study.dto.FileDTO;
import com.study.condition.BoardSelectCondition;
import com.study.form.*;
import com.study.returnType.BoardListAndCount;
import com.study.service.BoardService;
import com.study.service.CategoryService;
import com.study.service.CommentService;
import com.study.service.FileService;
import com.study.utils.EncryptUtils;
import com.study.utils.StringUtils;
import com.study.validate.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Board관련 Controller
 */
@Controller
public class BoardController {
    private final BoardService boardService;
    private final CategoryService categoryService;
    private final FileService fileService;
    private final CommentService commentService;

    @Autowired
    public BoardController(BoardService boardService, CategoryService categoryService, FileService fileService, CommentService commentService) {
        this.boardService = boardService;
        this.categoryService = categoryService;
        this.fileService = fileService;
        this.commentService = commentService;
    }

    /**
     * 자유 게시판 - 목록 페이지
     *
     * @param model
     * @param searchForm
     * @param pageNum
     * @return
     */
    @GetMapping(value = {"/boards/free/list"})
    public String getBoardList(Model model, SearchForm searchForm, @RequestParam(defaultValue = "1") int pageNum) {
        // SearchCondition 설정
        if (searchForm == null) {
            searchForm = new SearchForm();
        }

        // 페이지네이션 정보 설정
        int pageSize = 10;
        int startRow = (pageNum - 1) * pageSize;

        // boardSelectCondition 설정
        BoardSelectCondition boardSelectCondition = BoardSelectCondition.builder()
                .startDate(StringUtils.parseToTimestampStart(searchForm.getStartDate()))
                .endDate(StringUtils.parseToTimestampEnd(searchForm.getEndDate()))
                .categoryId(searchForm.getCategoryId())
                .searchText(searchForm.getSearchText())
                .pageSize(pageSize)
                .startRow(startRow)
                .build();

        // 필요한 정보들 가져오기
        BoardListAndCount boardListAndCount = boardService.search(boardSelectCondition);
        List<CategoryDTO> categoryList = categoryService.getCategoryList();

        // 필요한 정보들 설정
        List<BoardDTO> boardList = boardListAndCount.getBoardList();
        int totalBoardCount = boardListAndCount.getRowCount();
        int totalPageNum = (int) Math.ceil((double) boardListAndCount.getRowCount() / (double) pageSize);

        // 정보들 넘겨주기
        model.addAttribute("boardList", boardList);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalPageNum", totalPageNum);
        model.addAttribute("totalBoardCount", totalBoardCount);
        model.addAttribute("searchForm", searchForm);

        return "list";
    }

    /**
     * 게시판 - 등록 페이지
     *
     * @param model
     * @return
     */
    @GetMapping(value = {"/board/free/write"})
    public String writeBoard(Model model) {
        // 필요한 정보 가져오기
        List<CategoryDTO> categoryList = categoryService.getCategoryList();

        // 정보 넘겨주기
        model.addAttribute("categoryList", categoryList);

        return "write";
    }

    /**
     * 게시판 등록 POST
     *
     * @param createBoardForm
     * @return
     * @throws Exception
     */
    // TODO : /board/create ?
    @PostMapping(value = {"/create"})
    public String createBoard(CreateBoardForm createBoardForm){
        // 유효성 검사
        try {
            Validator.validateBoardInput(createBoardForm);
        } catch (IllegalArgumentException e) {
            return "redirect:/boards/free/list";
        }

        // BoardDTO 설정
        BoardDTO board = BoardDTO.builder()
                .categoryId(createBoardForm.getCategoryId())
                .userName(createBoardForm.getUserName())
                .password(EncryptUtils.encryptPassword(createBoardForm.getPassword()))
                .title(createBoardForm.getTitle())
                .content(createBoardForm.getContent())
                .build();

        // board 저장
        boardService.addBoard(board);

        // file 저장
        try{
            fileService.uploadFile(createBoardForm.getFile(), board.getBoardId());
        }catch (IOException e){
            e.printStackTrace();
        }

        return "redirect:/boards/free/list";
    }

    /**
     * 게시판 - 보기 페이지
     * @param model
     * @param seq
     * @param pageNum
     * @return
     */
    @GetMapping(value = {"/boards/free/view/{seq}"})
    public String viewBoard(Model model, @PathVariable Long seq, @RequestParam int pageNum) {
        // view 증가
        boardService.increaseView(seq);

        // 필요한 정보 가져오기
        BoardDTO board = boardService.findBoard(seq);
        String categoryName = categoryService.findCategoryName(board.getCategoryId());
        List<FileDTO> fileList = fileService.findFiles(board.getBoardId());
        List<CommentDTO> commentList = commentService.getComments(seq);

        // 정보 넘겨주기
        model.addAttribute("board", board);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("fileList", fileList);
        model.addAttribute("commentList", commentList);
        model.addAttribute("pageNum", pageNum);

        return "view";
    }

    /**
     * 비밀번호 확인 AJAX 요청
     * @return
     */
    @PostMapping("/board/passwordConfirm/ajax")
    public ResponseEntity<String> getPasswordAjax(@RequestBody AjaxPwValidateForm ajaxPwValidateForm){
        // 비밀번호 설정
        String inputPassword = EncryptUtils.encryptPassword(ajaxPwValidateForm.getPassword());
        String dbPassword = boardService.findPassword(ajaxPwValidateForm.getBoardId());

        // 비밀번호 불일치
        if (!inputPassword.equals(dbPassword)) {
            return new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST);
        }

        //비밀번호 일치
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /**
     * 게시판 - 수정 페이지
     */
    @GetMapping("/board/free/modify/{boardId}")
    public String modifyBoard(Model model, @PathVariable Long boardId, @RequestParam int pageNum){
        // 정보 가져오기
        BoardDTO board = boardService.findBoard(boardId);
        String categoryName = categoryService.findCategoryName(board.getCategoryId());
        List<FileDTO> fileList = fileService.findFiles(boardId);

        // 정보 넘겨주기
        model.addAttribute("board", board);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("fileList", fileList);
        model.addAttribute("pageNum", pageNum);

        return "modify";
    }

    /**
     * 게시물 수정
     * @param updateForm
     * @return
     */
    @PostMapping(value = {"/board/update"})
    public String updateBoard(UpdateForm updateForm){
        // boardDTO 설정
        BoardDTO board = BoardDTO.builder()
                .boardId(updateForm.getBoardId())
                .userName(updateForm.getUserName())
                .title(updateForm.getTitle())
                .content(updateForm.getContent())
                .build();

        // 게시물 수정
        boardService.modifyBoard(board);

        // 파일 삭제
        fileService.deleteSelectedFiles(updateForm.getDeleteFileIds());

        return "redirect:/boards/free/view/" + updateForm.getBoardId() + "?pageNum=" + updateForm.getPageNum();
    }

    /**
     * 게시물 삭제
     * @param boardId
     * @param pageNum
     * @return
     */
    @GetMapping(value = {"/board/delete/{boardId}"})
    public String deleteBoard(@PathVariable Long boardId,@RequestParam int pageNum){
        // 삭제
        commentService.deleteComments(boardId);
        fileService.deleteFiles(boardId);
        boardService.deleteBoard(boardId);

        return "redirect:/boards/free/list" + "?pageNum=" + pageNum;
    }
}
