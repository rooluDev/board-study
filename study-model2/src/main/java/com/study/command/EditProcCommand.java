package com.study.command;

import com.study.DAO.BoardDAO;
import com.study.DTO.BoardDTO;
import com.study.condition.EditCondition;
import com.study.validate.BoardFormValidate;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditProcCommand implements Command {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        EditCondition editCondition = new EditCondition();
        editCondition.setUserName(userName);
        editCondition.setTitle(title);
        editCondition.setContent(content);
        BoardDAO boardDAO = new BoardDAO();
        BoardFormValidate boardFormValidate = new BoardFormValidate();
        BoardDTO board = boardDAO.findById(boardId);
        if (!boardFormValidate.validatePasswordMatch(password, board.getPassword())) {
            response.sendRedirect("/board?command=error");
        }

        boardDAO.editBoard(boardId, board, editCondition);
        response.sendRedirect("/board?command=view&boardId=" + boardId + "&pageNum=" + pageNum);
    }
}
