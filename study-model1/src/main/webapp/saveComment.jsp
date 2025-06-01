<%@ page import="com.study.dto.Comment" %>
<%@ page import="com.study.repository.CommentRepository" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String comment = request.getParameter("comment");
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");
    String categoryId = request.getParameter("categoryId");
    String searchText = request.getParameter("searchText");
    int boardId = Integer.parseInt(request.getParameter("boardId"));
    int pageNum = Integer.parseInt(request.getParameter("pageNum"));

    CommentRepository commentRepository = CommentRepository.getInstance();

    Comment addedComment = Comment.builder()
            .boardId(boardId)
            .comment(comment)
            .build();

    commentRepository.insertComment(addedComment);

    response.sendRedirect("board.jsp?boardId=" + boardId + "&startDate=" + startDate + "&endDate=" + endDate + "&categoryId=" + categoryId + "&searchText=" + searchText + "&pageNum=" + pageNum);
%>