package com.study.command;

import com.study.DAO.BoardDAO;
import com.study.DAO.CommentDAO;
import com.study.DAO.FileDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 게시판 삭제 프로세스
 */
public class DeleteCommand implements Command {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int boardId = Integer.parseInt(request.getParameter("boardId"));

        BoardDAO boardDAO = new BoardDAO();
        CommentDAO commentDAO = new CommentDAO();
        FileDAO fileDAO = new FileDAO();

        commentDAO.deleteCommentsByBoardId(boardId);
        fileDAO.deleteByBoardId(boardId);
        boardDAO.deleteById(boardId);

        response.sendRedirect("/board?command=list");
    }
}
