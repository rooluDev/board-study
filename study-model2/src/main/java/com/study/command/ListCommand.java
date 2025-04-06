package com.study.command;

import com.study.DAO.BoardDAO;
import com.study.DAO.CategoryDAO;
import com.study.DTO.BoardDTO;
import com.study.DTO.CategoryDTO;
import com.study.condition.SearchCondition;
import com.study.utils.ConditionUtils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 게시판 리스트 페이지 이동
 */
public class ListCommand implements Command {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = "/WEB-INF/views/list.jsp";

        String pageNum = request.getParameter("pageNum");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String categoryId = request.getParameter("categoryId");
        String searchText = request.getParameter("searchText");

        SearchCondition searchCondition = ConditionUtils.parameterToSearchCondition(startDate, endDate, categoryId, searchText, pageNum);

        BoardDAO boardDAO = new BoardDAO();
        CategoryDAO categoryDAO = new CategoryDAO();

        int pageSize = 10;
        int countBoard = boardDAO.selectRowCountForBoardList(searchCondition);
        int totalPageNum = (int) Math.ceil((double) countBoard / (double) pageSize);

        List<CategoryDTO> categoryList = categoryDAO.getCategoryList();
        List<BoardDTO> boardList = boardDAO.selectBoardList(searchCondition);

        request.setAttribute("searchCondition", searchCondition);
        request.setAttribute("categoryList", categoryList);
        request.setAttribute("boardList", boardList);
        request.setAttribute("totalPageNum", totalPageNum);
        request.setAttribute("countBoard", countBoard);

        request.getRequestDispatcher(path).forward(request, response);
    }
}
