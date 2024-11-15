package com.study.command;

import com.study.DAO.FileDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteFileCommand implements Command {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int fileId = Integer.parseInt(request.getParameter("fileId"));
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        FileDAO fileDAO = new FileDAO();
        fileDAO.deleteById(fileId);
        response.sendRedirect("/board?command=edit&boardId=" + boardId + "&pageNum=" + pageNum);
    }
}
