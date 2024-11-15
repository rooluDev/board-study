package com.study.command;

import com.study.DAO.CategoryDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddCommand implements Command {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = "/WEB-INF/views/add.jsp";
        CategoryDAO categoryDAO = new CategoryDAO();
        request.setAttribute("categoryList", categoryDAO.getCategoryList());
        request.getRequestDispatcher(path).forward(request, response);
    }
}

