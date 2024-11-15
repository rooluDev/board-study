<%@ page import="com.study.DTO.BoardDTO" %>
<%@ page import="com.study.DTO.CommentDTO" %>
<%@ page import="com.study.DAO.CategoryDAO" %>
<%@ page import="com.study.DAO.CommentDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.DTO.FileDTO" %>
<%@ page import="com.study.utils.TimestampUtils" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/24/24
  Time: 4:36 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int boardId = (int) request.getAttribute("boardId");
    int pageNum = (int) request.getAttribute("pageNum");
    BoardDTO board = (BoardDTO) request.getAttribute("board");
    String categoryName = (String) request.getAttribute("categoryName");
    List<CommentDTO> commentList = (List<CommentDTO>) request.getAttribute("commentList");
    List<FileDTO> fileList = (List<FileDTO>) request.getAttribute("fileList");
%>
<html>
<head>
    <title>Title</title>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
</head>
<body>
<h1>게시판 - 보기</h1>
<%=board.getUserName()%>
<p>등록일시</p>
<%=TimestampUtils.parseToString(board.getCreatedAt(),"yyyy.MM.dd hh:dd")%>
<p>수정일시</p>
<%
    if (board.getEditedAt() != null) {
%>
<%= TimestampUtils.parseToString(board.getEditedAt(),"yyyy.MM.dd hh:dd")%>
<%
} else {
%>
<span>-</span>
<%
    }
%>
<br/>
<%=categoryName%>
<%=board.getTitle()%>
<%=board.getViews()%>
<br/>
<%=board.getContent()%>
<br/>
<%
    for(FileDTO file : fileList){
%>
        <a href="/board?command=download&fileId=<%=file.getFileId()%>&boardId=<%=boardId%>&pageNum=<%=pageNum%>"><%=file.getOriginalName()%></a>
        <br/>
<%
    }
%>
<br/>
<%
    for (CommentDTO comment : commentList) {
%>
<%=TimestampUtils.parseToString(comment.getCreatedAt(),"yyyy.MM.dd hh:dd")%>
<br/>
<%=comment.getComment()%>
<br/>
<span>--------------------------</span>
<br/>
<%
    }
%>
<form action="/board?command=commentProc" method="post">
    <input type="text" name="comment" placeholder="댓글을 입력해주세요">
    <input type="hidden" name="boardId" value="<%=boardId%>">
    <input type="hidden" name="pageNum" value="<%=pageNum%>">
    <input type="submit">
</form>
<br/>
<button type="button" onclick="goToList(<%=pageNum%>)">목록</button>
<button type="button" onclick="goToEdit(<%=boardId%> , <%=pageNum%>)">수정</button>
<button type="button" id="verifyPasswordBtn">삭제</button>

<div id="passwordVerificationModal" style="display: none;">
    <label for="passwordInput">Enter Password:</label>
    <input type="password" id="passwordInput">
    <button id="confirmPasswordBtn">Confirm</button>
    <button id="closeModalBtn">Close</button>
</div>

</body>
<script>
    function goToList(pageNum) {
        location.href = "/board?command=list&pageNum=" + pageNum;
    }

    function goToEdit(boardId, pageNum) {
        location.href = "/board?command=edit&boardId=" + boardId + "&pageNum=" + pageNum;
    }

    function deleteBoard(boardId, pageNum) {
        location.href = "/board?command=delete&boardId=" + boardId + "&pageNum=" + pageNum;
    }

    $(document).ready(function() {
        // 버튼을 클릭하면 모달 창을 띄움
        $("#verifyPasswordBtn").click(function() {
            $("#passwordVerificationModal").show();
        });

        // 확인 버튼을 클릭하면 AJAX 요청을 보냄
        $("#confirmPasswordBtn").click(function() {
            var enteredPassword = $("#passwordInput").val();

            // AJAX 요청
            $.ajax({
                type: "POST",
                url: "/board?command=passwordConfirm&boardId=<%=boardId%>",
                data: { password: enteredPassword },
                success: function(response) {
                    if (response === "success") {
                        deleteBoard(<%=boardId%>,<%=pageNum%>)
                    } else {
                        alert("Password is incorrect!");
                    }

                    // 모달 창 숨기기
                    $("#passwordVerificationModal").hide();
                },
                error: function() {
                    alert("Error during password verification.");
                }
            });
        });

        // 모달 창 닫기 버튼
        $("#closeModalBtn").click(function() {
            $("#passwordVerificationModal").hide();
        });
    });
</script>
</html>