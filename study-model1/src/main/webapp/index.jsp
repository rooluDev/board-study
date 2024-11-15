<%@ page import="com.study.connection.JDBCConnection" %>
<%@ page import="com.study.connection.JDBCConnection" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Hello World</title>
</head>
<body>
<h1><%= "Hello World!" %>
</h1>
<br/>
<a href="hello-servlet">Hello Servlet</a>


<%

    JDBCConnection t = new JDBCConnection();
    out.println(t.getConnection());

%>

</body>
</html>
