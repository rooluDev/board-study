<%@ page import="com.study.DTO.BoardDTO" %>
<%@ page import="com.study.DTO.CommentDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.DTO.FileDTO" %>
<%@ page import="com.study.utils.TimestampUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    BoardDTO board = (BoardDTO) request.getAttribute("board");
    List<CommentDTO> commentList = (List<CommentDTO>) request.getAttribute("commentList");
    List<FileDTO> fileList = (List<FileDTO>) request.getAttribute("fileList");

    String pageNum = (String) request.getAttribute("pageNum");
    String startDate = (String) request.getAttribute("startDate");
    String endDate = (String) request.getAttribute("endDate");
    String categoryId = (String) request.getAttribute("categoryId");
    String searchText = (String) request.getAttribute("searchText");
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
<%=TimestampUtils.parseToString(board.getCreatedAt(), "yyyy.MM.dd hh:dd")%>
<p>수정일시</p>
<%
    if (board.getEditedAt() != null) {
%>
<%= TimestampUtils.parseToString(board.getEditedAt(), "yyyy.MM.dd hh:dd")%>
<%
} else {
%>
<span>-</span>
<%
    }
%>
<br/>
<%=board.getCategoryName()%>
<%=board.getTitle()%>
<br/>
<span>조회수 : </span>
<%=board.getViews()%>
<br/>
<%=board.getContent()%>
<br/>
<%
    for (FileDTO file : fileList) {
%>
<a href="/board?command=download&fileId=<%=file.getFileId()%>"><%=file.getOriginalName()%>
</a>
<br/>
<%
    }
%>
<br/>
<%
    for (CommentDTO comment : commentList) {
%>
<%=TimestampUtils.parseToString(comment.getCreatedAt(), "yyyy.MM.dd hh:dd")%>
<br/>
<%=comment.getComment()%>
<br/>
<span>--------------------------</span>
<br/>
<%
    }
%>
<form action="/board?command=commentProc&boardId=<%=board.getBoardId()%>&pageNum=<%=pageNum%>&startDate=<%=startDate%>&endDate=<%=endDate%>&categoryId=<%=categoryId%>&searchText=<%=searchText%>"
      method="post">
    <input type="text" name="comment" placeholder="댓글을 입력해주세요">
    <input type="submit">
</form>
<br/>
<button type="button" onclick="goToList()">목록</button>
<button type="button" id="editBtn">수정</button>
<button type="button" id="deleteBtn">삭제</button>

<div id="passwordVerificationModal" style="display: none;">
    <label for="passwordInput">비밀번호를 입력하세요 :</label>
    <input type="password" id="passwordInput">
    <button id="confirmPasswordBtn">확인</button>
    <button id="closeModalBtn">취소</button>
</div>

</body>
<script>
    let actionType = "";

    function goToList() {
        location.href = "/board?command=list&pageNum=<%=pageNum%>&startDate=<%=startDate%>&endDate=<%=endDate%>&categoryId=<%=categoryId%>&searchText=<%=searchText%>";
    }

    function goToEdit() {
        location.href = "/board?command=edit&boardId=<%=board.getBoardId()%>&pageNum=<%=pageNum%>&startDate=<%=startDate%>&endDate=<%=endDate%>&categoryId=<%=categoryId%>&searchText=<%=searchText%>";
    }

    function deleteBoard(boardId) {
        location.href = "/board?command=delete&boardId=" + boardId;
    }

    $(document).ready(function () {
        $("#editBtn").click(function () {
            actionType = "edit";
            $("#passwordVerificationModal").show();
        });

        $("#deleteBtn").click(function () {
            actionType = "delete";
            $("#passwordVerificationModal").show();
        });

        $("#closeModalBtn").click(function () {
            $("#passwordVerificationModal").hide();
        });

        $("#confirmPasswordBtn").click(function () {
            const enteredPassword = $("#passwordInput").val();

            // AJAX 요청
            $.ajax({
                type: "POST",
                url: "/board?command=passwordConfirm&boardId=<%=board.getBoardId()%>",
                data: {password: enteredPassword},
                success: function (response) {
                    if (response === "success") {
                        if (actionType === "edit") {
                            goToEdit();
                        } else if (actionType === "delete") {
                            deleteBoard(<%=board.getBoardId()%>);
                        }
                    } else {
                        alert("비밀번호가 일치하지 않습니다.");
                    }
                    // 모달 창 숨기기
                    $("#passwordVerificationModal").hide();
                },
                error: function () {
                    alert("error");
                }
            });
        });
    });

</script>
</html>