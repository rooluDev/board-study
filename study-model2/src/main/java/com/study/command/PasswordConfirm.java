package com.study.command;

import com.study.DAO.BoardDAO;
import com.study.DTO.BoardDTO;
import com.study.validate.BoardFormValidate;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PasswordConfirm implements Command {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String enteredPassword = request.getParameter("password");
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        BoardDAO boardDAO = new BoardDAO();
        BoardDTO board = boardDAO.findById(boardId);
        if (this.verifyPassword(enteredPassword, board)) {
            response.getWriter().write("success");
        } else {
            response.getWriter().write("failure");
        }

    }

    private boolean verifyPassword(String enteredPassword, BoardDTO board) {
        BoardFormValidate boardFormValidate = new BoardFormValidate();
        return boardFormValidate.validatePasswordMatch(enteredPassword, board.getPassword());
    }
}
