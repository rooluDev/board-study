package com.study.command;

import com.study.DAO.BoardDAO;
import com.study.DAO.CommentDAO;
import com.study.DAO.FileDAO;
import com.study.DTO.BoardDTO;
import com.study.DTO.CommentDTO;
import com.study.DTO.FileDTO;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 게시판 보기 페이지 이동
 */
public class ViewCommand implements Command {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = "/WEB-INF/views/board.jsp";

        String pageNum = request.getParameter("pageNum");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String categoryId = request.getParameter("categoryId");
        String searchText = request.getParameter("searchText");
        int boardId = Integer.parseInt(request.getParameter("boardId"));

        BoardDAO boardDAO = new BoardDAO();
        CommentDAO commentDAO = new CommentDAO();
        FileDAO fileDAO = new FileDAO();

        BoardDTO board = boardDAO.selectBoardById(boardId);
        boardDAO.plusViewById(boardId);

        List<CommentDTO> commentList = commentDAO.selectCommentListByBoardId(boardId);
        List<FileDTO> fileList = fileDAO.selectFileListByBoardId(boardId);

        request.setAttribute("board", board);
        request.setAttribute("commentList", commentList);
        request.setAttribute("fileList", fileList);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("searchText", searchText);

        request.getRequestDispatcher(path).forward(request, response);
    }
}
