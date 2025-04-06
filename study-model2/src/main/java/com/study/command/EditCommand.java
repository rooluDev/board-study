package com.study.command;

import com.study.DAO.BoardDAO;
import com.study.DAO.FileDAO;
import com.study.DTO.BoardDTO;
import com.study.DTO.FileDTO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 수정 페이지로 이동
 */
public class EditCommand implements Command {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = "/WEB-INF/views/edit.jsp";

        int boardId = Integer.parseInt(request.getParameter("boardId"));
        String pageNum = request.getParameter("pageNum");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String categoryId = request.getParameter("categoryId");
        String searchText = request.getParameter("searchText");


        BoardDAO boardDAO = new BoardDAO();
        FileDAO fileDAO = new FileDAO();
        BoardDTO board = boardDAO.selectBoardById(boardId);
        List<FileDTO> fileList = fileDAO.selectFileListByBoardId(boardId);

        request.setAttribute("board", board);
        request.setAttribute("fileList", fileList);
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("searchText", searchText);

        request.getRequestDispatcher(path).forward(request, response);
    }
}
