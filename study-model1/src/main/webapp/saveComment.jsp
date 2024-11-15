<%@ page import="com.study.repository.CommentRepository" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.domain.Comment" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/20/24
  Time: 3:02 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String comment = request.getParameter("comment");
    int boardId = Integer.parseInt(request.getParameter("boardId"));
    int pageNum = Integer.parseInt(request.getParameter("pageNum"));
    CommentRepository commentRepository = new CommentRepository();
    int commentId = commentRepository.insertBoard(comment,boardId);
    response.sendRedirect("board.jsp?boardId=" + boardId + "&pageNum=" + pageNum);
%>
<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html>
