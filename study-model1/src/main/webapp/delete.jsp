<%@ page import="com.study.repository.BoardRepository" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/19/24
  Time: 4:20 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int boardId = Integer.parseInt(request.getParameter("boardId"));
    BoardRepository boardRepository = new BoardRepository();
    boardRepository.deleteById(boardId);
    response.sendRedirect("list.jsp");
%>
<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html>
