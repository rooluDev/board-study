<%@ page import="com.study.DTO.BoardDTO" %>
<%@ page import="com.study.DTO.FileDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.utils.TimestampUtils" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/25/24
  Time: 12:59 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    BoardDTO board = (BoardDTO) request.getAttribute("board");
    String categoryName = (String)request.getAttribute("categoryName");
    int pageNum = (int)request.getAttribute("pageNum");
    int boardId = (int) request.getAttribute("boardId");
    List<FileDTO> fileList = (List<FileDTO>) request.getAttribute("fileList");
%>
<html>
<head>
    <title>Title</title>
</head>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<body>
    <%=categoryName%>
    </br>
    <%=TimestampUtils.parseToString(board.getCreatedAt(),"yyyy.MM.dd hh:dd")%>
    </br>
    <%
        if (board.getEditedAt() != null) {
    %>
    <%=TimestampUtils.parseToString(board.getEditedAt(),"yyyy.MM.dd hh:dd")%>
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
<form action="/board?command=editProc" method="post">
    <label for="user_name">작성자</label>
    <input type="text" id="user_name" name="userName" value="<%=board.getUserName()%>" placeholder="작성자">
    <br/>
    <label for="password">비밀번호</label>
    <input type="password" id="password" name="password" placeholder="비밀번호">
    <br/>
    <label for="title">제목</label>
    <input type="text" id="title" name="title" value="<%=board.getTitle()%>">
    <br/>
    <label for="content">내용</label>
    <input type="text" id="content" name="content" value="<%=board.getContent()%>">
    <input type="hidden" name="boardId" value="<%=boardId%>">
    <input type="hidden" name="pageNum" value="<%=pageNum%>">
    <br/>
    <%
        for(FileDTO file : fileList){
    %>
    <%=file.getOriginalName()%>
    <button type="button" onclick="deleteFile(<%=file.getFileId()%>,<%=boardId%>,<%=pageNum%>)">X</button>
    <%
        }
    %>
    <br/>
    <button type="button" onclick="goToBoard(<%=boardId%>,<%=pageNum%>)">취소</button>
    <button type="submit">저장</button>
</form>

</body>
<script>
    function goToBoard(boardId, pageNum) {
        location.href = "board?command=view&boardId=" + boardId + "&pageNum=" + pageNum;
    }

    function deleteFile(fileId, boardId, pageNum) {
        location.href = "board?command=deleteFile&fileId=" + fileId + "&boardId=" + boardId + "&pageNum=" + pageNum;
    }
</script>
</html>
