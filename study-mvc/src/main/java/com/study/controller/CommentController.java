package com.study.controller;

import com.study.form.CommentAddForm;
import com.study.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Comment관련 controller
 */
@Controller
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * comment 추가
     * @param commentAddForm
     * @return
     */
    @PostMapping(value = {"/comment/add"})
    public String addComment(CommentAddForm commentAddForm){
        // 댓글 저장
        commentService.createComment(commentAddForm.getBoardId(),commentAddForm.getComment());

        return "redirect:/boards/free/view/" + commentAddForm.getBoardId() + "?pageNum=" + commentAddForm.getPageNum();
    }

}
