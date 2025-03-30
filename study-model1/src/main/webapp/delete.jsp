<%@ page import="com.study.service.BoardService" %>
<%@ page import="com.study.service.CommentService" %>
<%@ page import="com.study.service.FileService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int boardId = Integer.parseInt(request.getParameter("boardId"));

    BoardService boardService = new BoardService();
    CommentService commentService = new CommentService();
    FileService fileService = new FileService();

    commentService.deleteCommentsByBoardId(boardId);
    fileService.deleteByBoardId(boardId);
    boardService.deleteBoardById(boardId);

    response.sendRedirect("list.jsp");
%>