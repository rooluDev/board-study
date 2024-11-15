package com.study.command;

import com.study.DAO.CommentDAO;
import com.study.DTO.CommentDTO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommentProc implements Command {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String comment = request.getParameter("comment");
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setBoardId(boardId);
        commentDTO.setComment(comment);
        CommentDAO commentDAO = new CommentDAO();
        commentDAO.insertComment(commentDTO);
        response.sendRedirect("/board?command=view&boardId=" + boardId + "&pageNum=" + pageNum);
    }
}
