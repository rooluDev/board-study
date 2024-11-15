<%@ page import="com.study.repository.BoardRepository" %>
<%@ page import="com.study.domain.Board" %>
<%@ page import="com.study.repository.CategoryRepository" %>
<%@ page import="com.study.repository.CommentRepository" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.domain.Comment" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/18/24
  Time: 6:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  BoardRepository boardRepository = new BoardRepository();
  CategoryRepository categoryRepository = new CategoryRepository();
  CommentRepository commentRepository = new CommentRepository();
  int boardId = Integer.parseInt(request.getParameter("boardId"));
  int pageNum = Integer.parseInt(request.getParameter("pageNum"));
  Board board = boardRepository.findById(boardId);
  boardRepository.plusViews(boardId);
  String category = categoryRepository.findByBoardId(boardId);
  List<Comment> commentList = commentRepository.commentList(boardId);
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
  if(board.getEditedAt() != null){
%>
<%= board.getEditedAt()%>
<%
  }else{
%>
<span>-</span>
<%
  }
%>
<br/>
  <%=category%>
  <%=board.getTitle()%>
  <%=board.getViews()%>
<br/>
  <%=board.getContent()%>
<br/>
<%
  for(Comment comment : commentList){
%>
<%=comment.getCreatedAt()%>
<br/>
<%=comment.getComment()%>
<p>--------------------------</p>
<%
  }
%>
  <form action="saveComment.jsp">
    <input type="text" name="comment" placeholder="댓글을 입력해주세요">
    <input type="hidden" name="boardId" value="<%=boardId%>">
    <input  type="hidden" name="pageNum" value="<%=pageNum%>">
    <input type="submit">
  </form>
<br/>
<button type="button" onclick="goToList(<%=pageNum%>)">목록</button>
<button type="button" onclick="goToEdit(<%=boardId%> , <%=pageNum%>)">수정</button>
<button type="button" onclick="deleteBoard(<%=boardId%>)">삭제</button>
</body>
<script>
  function goToList(pageNum){
    const listPage = "list.jsp";
    location.href = listPage + "?pageNum=" + pageNum;
  }
  function goToEdit(boardId,pageNum){
    const editPage = "edit.jsp";
    location.href = editPage + "?boardId=" + boardId + "&pageNum=" + pageNum;
  }

  function deleteBoard(boardId){
    const deletePage = "delete.jsp";
    location.href = deletePage + "?boardId=" + boardId;
  }
</script>
</html>
