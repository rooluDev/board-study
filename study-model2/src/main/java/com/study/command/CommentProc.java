package com.study.command;

import com.study.DAO.CommentDAO;
import com.study.DTO.CommentDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 댓글 추가 프로세스
 */
public class CommentProc implements Command {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String comment = request.getParameter("comment");
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String categoryId = request.getParameter("categoryId");
        String searchText = request.getParameter("searchText");


        CommentDTO commentDTO = CommentDTO.builder()
                .boardId(boardId)
                .comment(comment)
                .build();

        CommentDAO commentDAO = new CommentDAO();
        commentDAO.insertComment(commentDTO);

        response.sendRedirect("/board?command=view&boardId=" + boardId + "&pageNum=" + pageNum + "&startDate=" + startDate + "&endDate=" + endDate + "&categoryId=" + categoryId + "&searchText=" + searchText);
    }
}
