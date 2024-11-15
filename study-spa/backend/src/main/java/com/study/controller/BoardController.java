package com.study.controller;

import com.study.condition.BoardSelectCondition;
import com.study.dto.*;
import com.study.exception.PasswordIncorrectException;
import com.study.exception.common.success.ApiResponse;
import com.study.exception.common.success.SuccessCode;
import com.study.service.BoardService;
import com.study.service.CommentService;
import com.study.service.FileService;
import com.study.utils.EncryptUtils;
import com.study.utils.StringUtils;
import com.study.validate.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * board rest api controller
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final FileService fileService;
    private final CommentService commentService;

    @Autowired
    public BoardController(BoardService boardService, FileService fileService, CommentService commentService) {
        this.boardService = boardService;
        this.fileService = fileService;
        this.commentService = commentService;
    }

    /**
     * 검색 조건에 따른 게시물 리스트 요청
     *
     * @param boardSearchFormDto
     * @return
     */
    @GetMapping("/boards")
    public ApiResponse<BoardListDtoForListPage> getBoardList(@ModelAttribute BoardSearchFormDto boardSearchFormDto) {
        // TODO : 따로 함수화
        // /boards/free/list no param
        if (StringUtils.isBoardFormNull(boardSearchFormDto)) {
            String startDate = LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String endDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            boardSearchFormDto = new BoardSearchFormDto(startDate, endDate, -1L, "", 1, 10);
        }

        // DB SELECT OFFSET Setting
        int startRow = (boardSearchFormDto.getPageNum() - 1) * boardSearchFormDto.getPageSize();

        // boardSelectCondition 설정
        // 만든 이유 : dto에는 로직이 없어야한다. 받아온 String 값을 Timestamp로 변경이 불가 -> 맞는지
        // String -> Timestamp : startDate = 00:00:00, endDate = 23:59:59
        BoardSelectCondition boardSelectCondition = BoardSelectCondition.builder()
                .startDate(StringUtils.parseToTimestampStart(boardSearchFormDto.getStartDate()))
                .endDate(StringUtils.parseToTimestampEnd(boardSearchFormDto.getEndDate()))
                .categoryId(boardSearchFormDto.getCategoryId())
                .searchText(boardSearchFormDto.getSearchText())
                .pageSize(boardSearchFormDto.getPageSize())
                .startRow(startRow)
                .build();

        // 응답 Dto 설정
        BoardListDtoForListPage boardListDTOForListPage = BoardListDtoForListPage.builder()
                .searchCondition(boardSearchFormDto)
                .boardList(boardService.getBoardList(boardSelectCondition))
                .totalCount(boardService.getBoardCount(boardSelectCondition))
                .build();

        return new ApiResponse(boardListDTOForListPage, SuccessCode.SELECT_SUCCESS);
    }

    /**
     * 게시물 요청
     *
     * @param boardId
     * @param option
     * @return
     */
    @GetMapping("/board/{boardId}")
    public ApiResponse<BoardDto> getBoard(@PathVariable Long boardId, @RequestParam String option) throws Exception {
        // 필요한 정보 요청
        BoardDto boardDTO = boardService.findBoard(boardId);
        // 조회수 증가 - 보기 페이지
        // TODO : 따로
        if (option.equals("view")) {
            boardService.increaseView(boardId);
        }
        return new ApiResponse(boardDTO, SuccessCode.SELECT_SUCCESS);
    }

    /**
     * 게시물 생성
     *
     * @param boardCreateFormDto
     * @return
     * @throws Exception
     */
    @PostMapping("/board")
    public ApiResponse<String> uploadBoard(@ModelAttribute BoardCreateFormDto boardCreateFormDto) throws Exception {
        // 저장할 Board DTO 생성
        BoardDto boardDTO = BoardDto.builder()
                .categoryId(boardCreateFormDto.getCategoryId())
                .userName(boardCreateFormDto.getUserName())
                .password(boardCreateFormDto.getPassword())
                .title(boardCreateFormDto.getTitle())
                .content(boardCreateFormDto.getContent())
                .build();

        // 유효성 검사
        Validator.validateBoardInput(boardDTO, boardCreateFormDto.getPasswordCheck());

        // 비밀번호 암호화
        boardDTO.setPassword(EncryptUtils.encryptPassword(boardDTO.getPassword()));

        // DB에 board 저장
        boardService.addBoard(boardDTO);

        // File 저장
        // TODO : multipart null check

        fileService.addFile(boardCreateFormDto.getFiles(), boardDTO.getBoardId());

        return new ApiResponse(boardDTO, SuccessCode.INSERT_SUCCESS);
    }

    /**
     * 비밀번호 확인
     *
     * @param passwordCheckDto
     * @return
     * @throws Exception
     */
    // /board/id/check-password -> 객체 생성시 멤버변수 password 하나 -> id와 password로 바꿈
    @PostMapping("/board/check-password")
    public ApiResponse<String> checkPassword(@RequestBody PasswordCheckDto passwordCheckDto) {
        // boardId로 비밀번호 가져오기
        BoardDto boardDTO = boardService.findBoard(passwordCheckDto.getBoardId());
        // TODO : NPE
        // 비밀번호 확인
        String password = EncryptUtils.encryptPassword(passwordCheckDto.getPassword());
        if (!boardDTO.getPassword().equals(password)) {
            throw new PasswordIncorrectException();
        }
        return new ApiResponse("success", SuccessCode.VERIFICATION_SUCCESS);
    }

    /**
     * 게시물 삭제
     *
     * @param boardId
     * @return
     */
    @DeleteMapping("/board/{boardId}")
    public ApiResponse<String> deleteBoard(@PathVariable Long boardId) {
        // delete에는 body가 없다 -> password를 param으로 넘겨야하는데 비밀번호 유출의 위험 -> ??
        // header
        // TODO : header
        commentService.deleteByBoardId(boardId);
        fileService.deleteFilesByBoardId(boardId);
        boardService.deleteBoardById(boardId);

        return new ApiResponse("success", SuccessCode.DELETE_SUCCESS);
    }

    /**
     * 게시물 업데이트
     *
     * @param boardId
     * @param boardUpdateFormDto
     * @return
     */
    @PutMapping("/board/{boardId}")
    public ApiResponse<String> updateBoard(@PathVariable Long boardId, @RequestBody BoardUpdateFormDto boardUpdateFormDto) throws Exception {
        // boardId 확인
        String password = boardService.findBoard(boardId).getPassword();
        // 유효성 검증
        Validator.validateUpdateBoardInput(boardUpdateFormDto, password);
        // boardDTO 설정
        BoardDto board = BoardDto.builder()
                .boardId(boardId)
                .userName(boardUpdateFormDto.getUserName())
                .title(boardUpdateFormDto.getTitle())
                .content(boardUpdateFormDto.getContent())
                .build();

        // 업데이트
        boardService.updateBoard(board);

        return new ApiResponse("success", SuccessCode.UPDATE_SUCCESS);
    }
}
