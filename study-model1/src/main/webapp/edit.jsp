<%@ page import="com.study.domain.Board" %>
<%@ page import="com.study.repository.BoardRepository" %>
<%@ page import="com.study.repository.CategoryRepository" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/16/24
  Time: 3:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int boardId = Integer.parseInt(request.getParameter("boardId"));
    int pageNum = Integer.parseInt(request.getParameter("pageNum"));
    BoardRepository boardRepository = new BoardRepository();
    CategoryRepository categoryRepository = new CategoryRepository();
    Board board = boardRepository.findById(boardId);
    String category = categoryRepository.findByBoardId(boardId);
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="editData.jsp">
        <%=category%>
        </br>
        <%=board.getCreatedAt()%>
        </br>
        <%
            if (board.getEditedAt() != null) {
        %>
        <%=board.getEditedAt()%>
        <%
            }else{
        %>
            <span>-</span>
        <%
            }
        %>
        <br/>
        <span>조회수</span><%=board.getViews()%>
        </br>
        <label for="user_name">작성자</label>
        <input type="text" id="user_name" name="user_name" value="<%=board.getUserName()%>" placeholder="작성자">
        <br/>
        <label for="password">비밀번호</label>
        <input type="password" id="password" name="password" placeholder="비밀번호">
        <br/>
        <label for="title">제목</label>
        <input type="text" id="title" name="title" value="<%=board.getTitle()%>">
        <br/>
        <label for="content">내용</label>
        <input type="text" id="content" name="content" value="<%=board.getContent()%>">
        <br/>
        <button type="button" onclick="goToBoard(<%=boardId%>)">취소</button>
        <button type="submit">저장</button>
    </form>
</body>
<script>
    function goToBoard(boardId){
        const boardPage = "board.jsp";
        location.href = boardPage + "?boardId=" + <%=boardId%> +"&pageNum=" + <%=pageNum%>;
    }
</script>
</html>
