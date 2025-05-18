package com.study.controller;

import com.study.dto.CommentDto;
import com.study.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * comment rest api controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    /**
     * 게시물에 등록된 댓글 리스트
     *
     * @param boardId pk
     * @return {
     *     commentList : []
     * }
     */
    @GetMapping("/comments/{boardId}")
    public ResponseEntity<List<CommentDto>> getCommentList(@PathVariable(name = "boardId") Long boardId) {

        List<CommentDto> commentList = commentService.getCommentList(boardId);

        return ResponseEntity.ok().body(commentList);
    }

    /**
     * 댓글 등록
     *
     * @param commentDto 추가할 댓글 리스트
     * @return null
     */
    @PostMapping("/comment")
    public ResponseEntity registerComment(@RequestBody CommentDto commentDto) {

        commentService.createComment(commentDto);

        return ResponseEntity.ok().body(null);
    }
}
