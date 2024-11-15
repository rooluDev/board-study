package com.study.command;

import com.study.DAO.BoardDAO;
import com.study.DAO.CategoryDAO;
import com.study.DTO.BoardDTO;
import com.study.DTO.CategoryDTO;
import com.study.condition.SearchCondition;
import com.study.utils.StringUtils;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListCommand implements Command {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = "/WEB-INF/views/list.jsp";
        String pageNumParam = request.getParameter("pageNum");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String category = request.getParameter("category");
        String searchText = request.getParameter("searchText");
        BoardDAO boardDAO = new BoardDAO();
        CategoryDAO categoryDAO = new CategoryDAO();
        SearchCondition searchCondition = new SearchCondition();
        int pageNum = 1;
        if (!StringUtils.isNull(pageNumParam)) {
            pageNum = Integer.parseInt(pageNumParam);
        }

        if (StringUtils.isNull(searchText)) {
            searchCondition.setSearchText((String)null);
            searchCondition.setCategoryId(-1);
        } else {
            searchCondition.setSearchText(searchText);
            searchCondition.setStartDate(StringUtils.parseToTimestamp(startDate));
            searchCondition.setEndDate(StringUtils.parseToTimestamp(endDate));
            searchCondition.setCategoryId(Integer.parseInt(category));
        }

        int pageSize = 5;
        int startRow = (pageNum - 1) * pageSize;
        int countBoard = boardDAO.countBoard(searchCondition);
        int totalPageNum = (int)Math.ceil((double)countBoard / (double)pageSize);
        List<BoardDTO> boardList = boardDAO.selectBoard(searchCondition, pageSize, startRow);
        List<CategoryDTO> categoryList = categoryDAO.getCategoryList();
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("searchCondition", searchCondition);
        request.setAttribute("totalPageNum", totalPageNum);
        request.setAttribute("countBoard", countBoard);
        request.setAttribute("boardList", boardList);
        request.setAttribute("categoryList", categoryList);
        request.setAttribute("pageNum", pageNum);
        request.getRequestDispatcher(path).forward(request, response);
    }
}
