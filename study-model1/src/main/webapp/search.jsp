<%@ page import="com.study.repository.BoardRepository" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.domain.Board" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/19/24
  Time: 9:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");
    String category = request.getParameter("category");
    String searchText = request.getParameter("searchText");
    response.sendRedirect("list.jsp?startDate=" + startDate+"&endDate="+endDate+"&category="+category+"&searchText="+searchText);

%>
<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html>
