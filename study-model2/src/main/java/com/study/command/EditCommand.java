package com.study.command;

import com.study.DAO.BoardDAO;
import com.study.DAO.CategoryDAO;
import com.study.DAO.FileDAO;
import com.study.DTO.BoardDTO;
import com.study.DTO.FileDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditCommand implements Command {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = "/WEB-INF/views/edit.jsp";
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        BoardDAO boardDAO = new BoardDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        FileDAO fileDAO = new FileDAO();
        BoardDTO board = boardDAO.findById(boardId);
        String categoryName = categoryDAO.findById(board.getCategoryId());
        List<FileDTO> fileList = fileDAO.findByBoardId(boardId);
        request.setAttribute("board", board);
        request.setAttribute("fileList", fileList);
        request.setAttribute("categoryName", categoryName);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("boardId", boardId);
        request.getRequestDispatcher(path).forward(request, response);
    }
}
