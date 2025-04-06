package com.study.command;

import com.study.DAO.CategoryDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 게시판 추가 페이지로 이동
 */
public class AddCommand implements Command {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = "/WEB-INF/views/add.jsp";

        String pageNum = request.getParameter("pageNum");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String categoryId = request.getParameter("categoryId");
        String searchText = request.getParameter("searchText");

        CategoryDAO categoryDAO = new CategoryDAO();

        request.setAttribute("categoryList", categoryDAO.getCategoryList());
        request.setAttribute("pageNum", pageNum);
        request.setAttribute("startDate", startDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("searchText", searchText);

        request.getRequestDispatcher(path).forward(request, response);
    }
}

