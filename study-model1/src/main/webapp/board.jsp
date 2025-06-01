<%@ page import="com.study.dto.Board" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.dto.Comment" %>
<%@ page import="com.study.dto.File" %>
<%@ page import="com.study.repository.BoardRepository" %>
<%@ page import="com.study.repository.CommentRepository" %>
<%@ page import="com.study.repository.FileRepository" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int boardId = Integer.parseInt(request.getParameter("boardId"));
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");
    String categoryId = request.getParameter("categoryId");
    String searchText = request.getParameter("searchText");
    String pageNum = request.getParameter("pageNum");

    BoardRepository boardRepository = BoardRepository.getInstance();
    Board board = boardRepository.selectBoardById(boardId);
    boardRepository.plusViews(boardId);

    CommentRepository commentRepository = CommentRepository.getInstance();
    List<Comment> commentList = commentRepository.selectCommentListByBoardId(boardId);

    FileRepository fileRepository = FileRepository.getInstance();
    List<File> fileList = fileRepository.selectFileListByBoardId(boardId);
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>게시판 - 보기</h1>
<%=board.getUserName()%>
<p>등록일시</p>
<%=board.getCreatedAt()%>
<p>수정일시</p>
<%
    if (board.getEditedAt() != null) {
%>
<%= board.getEditedAt()%>
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
    for (File file : fileList) {
%>
<div class="download-file" onclick="downloadFile(<%=file.getFileId()%>)">
    <%=file.getOriginalName()%>
</div>
<%
    }
%>

<%
    for (Comment comment : commentList) {
%>
<%=comment.getCreatedAt()%>
<br/>
<%=comment.getComment()%>
<p>--------------------------</p>
<%
    }
%>
<form action="saveComment.jsp" method="post">
    <input type="text" name="comment" placeholder="댓글을 입력해주세요">
    <input type="hidden" name="startDate" value="<%=startDate%>">
    <input type="hidden" name="endDate" value="<%=endDate%>">
    <input type="hidden" name="categoryId" value="<%=categoryId%>">
    <input type="hidden" name="searchText" value="<%=searchText%>">
    <input type="hidden" name="boardId" value="<%=boardId%>">
    <input type="hidden" name="pageNum" value="<%=pageNum%>">
    <button type="submit">등록</button>
</form>
<br/>
<button type="button" onclick="goToList()">목록</button>
<button type="button" onclick="goToEdit()">수정</button>
<button type="button" onclick="deleteBoard()">삭제</button>
</body>
<script>
    function goToList() {
        const listPage = "list.jsp";
        location.href = listPage + "?pageNum=<%=pageNum%>&startDate=<%=startDate%>&endDate=<%=endDate%>&categoryId=<%=categoryId%>&searchText=<%=searchText%>";
    }

    function goToEdit() {
        const inputPassword = prompt("비밀번호를 입력하세요.");

        const editLocation = "edit.jsp";

        location.href = "passwordConfirm.jsp?inputPassword=" + inputPassword + "&location=" + editLocation +
            "&boardId=<%=boardId%>&pageNum=<%=pageNum%>&startDate=<%=startDate%>&endDate=<%=endDate%>&categoryId=<%=categoryId%>&searchText=<%=searchText%>";
    }

    function deleteBoard() {
        const inputPassword = prompt("비밀번호를 입력하세요.");

        const deleteLocation = "delete.jsp";

        location.href = "passwordConfirm.jsp?inputPassword=" + inputPassword + "&location=" + deleteLocation +
            "&boardId=<%=boardId%>&pageNum=<%=pageNum%>&startDate=<%=startDate%>&endDate=<%=endDate%>&categoryId=<%=categoryId%>&searchText=<%=searchText%>";
    }

    function downloadFile(fileId) {
        const downloadFilePage = "download.jsp";
        location.href = downloadFilePage + "?fileId=" + fileId;
    }

</script>
<style>
    .download-file {
        cursor: pointer;
    }

    .download-file:hover {
        text-decoration: underline;
    }
</style>
</html>
