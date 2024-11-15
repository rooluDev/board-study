package com.study.command;

import com.study.DAO.BoardDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteCommand implements Command {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        BoardDAO boardDAO = new BoardDAO();
        boardDAO.deleteById(boardId);
        response.sendRedirect("/board?command=list&pageNum=" + pageNum);
    }
}
