package com.study.command;

import com.study.DAO.BoardDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 비밀번호 확인 프로세스
 */
public class PasswordConfirmCommand implements Command {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String enteredPassword = request.getParameter("password");
        int boardId = Integer.parseInt(request.getParameter("boardId"));

        BoardDAO boardDAO = new BoardDAO();
        int rowCount = boardDAO.selectBoardByPassword(boardId,enteredPassword);

        if (rowCount > 0) {
            response.getWriter().write("success");
        } else {
            response.getWriter().write("failure");
        }
    }
}
