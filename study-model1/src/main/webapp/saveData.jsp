<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/15/24
  Time: 6:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.study.repository.BoardRepository" %>
<%@page import="com.study.repository.FileRepository" %>
<%@ page import="com.study.validate.ValidateForm" %>
<%@ page import="com.study.repository.CategoryRepository" %>
<%
    String category = request.getParameter("category");
    String userName = request.getParameter("user_name");
    String password = request.getParameter("password");
    String passwordRe = request.getParameter("passwordRe");
    String title = request.getParameter("title");
    String content = request.getParameter("content");

    ValidateForm validateForm = new ValidateForm();
    if(!validateForm.validateCategory(category)){
        response.sendRedirect("posting.jsp");
    }
    if (!validateForm.validateUserName(userName)) {
        response.sendRedirect("posting.jsp");
    }
    if (!validateForm.validatePassword(password)) {
        response.sendRedirect("posting.jsp");
    }
    if (!validateForm.validatePasswordMatch(password, passwordRe)) {
        response.sendRedirect("posting.jsp");
    }
    if (!validateForm.validateTitle(title)) {
        response.sendRedirect("posting.jsp");
    }
    if (!validateForm.validateContent(content)) {
        response.sendRedirect("posting.jsp");
    }

    BoardRepository boardRepository = new BoardRepository();
    int boardId = boardRepository.insertBoard(title,content,userName,password);

    CategoryRepository categoryRepository = new CategoryRepository();
    categoryRepository.insertCategory(boardId,category);

    FileRepository fileRepository = new FileRepository();
    fileRepository.insertFile("null");
    response.sendRedirect("list.jsp");
%>