package com.study.command;

import com.study.DAO.BoardDAO;
import com.study.DAO.CategoryDAO;
import com.study.DAO.CommentDAO;
import com.study.DAO.FileDAO;
import com.study.DTO.BoardDTO;
import com.study.DTO.CommentDTO;
import com.study.DTO.FileDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewCommand implements Command {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = "/WEB-INF/views/board.jsp";

        int boardId = Integer.parseInt(request.getParameter("boardId"));
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));

        BoardDAO boardDAO = new BoardDAO();
        CommentDAO commentDAO = new CommentDAO();
        FileDAO fileDAO = new FileDAO();
        CategoryDAO categoryDAO = new CategoryDAO();

        BoardDTO board = boardDAO.findById(boardId);
        boardDAO.plusViewById(boardId);

        String categoryName = categoryDAO.findById(board.getCategoryId());

        List<CommentDTO> commentList = commentDAO.getCommentList(boardId);
        List<FileDTO> fileList = fileDAO.findByBoardId(boardId);

        request.setAttribute("boardId", boardId);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("board", board);
        request.setAttribute("categoryName", categoryName);
        request.setAttribute("commentList", commentList);
        request.setAttribute("fileList", fileList);
        request.getRequestDispatcher(path).forward(request, response);
    }
}
