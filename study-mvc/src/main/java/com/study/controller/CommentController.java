package com.study.controller;

import com.study.condition.SearchCondition;
import com.study.dto.CommentDTO;
import com.study.service.CommentService;
import com.study.utils.ConditionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Comment controller
 */
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * comment 추가
     *
     * @param comment 댓글
     * @return view
     */
    @PostMapping(value = {"/comment/add"})
    public String addComment(@ModelAttribute CommentDTO comment, @ModelAttribute SearchCondition searchCondition) {
        // 댓글 저장
        commentService.createComment(comment);

        return "redirect:/board/view/" + comment.getBoardId() + "?" + ConditionUtils.convertToQueryString(searchCondition);
    }

}
