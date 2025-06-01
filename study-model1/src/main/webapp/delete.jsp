<%@ page import="com.study.repository.BoardRepository" %>
<%@ page import="com.study.repository.CommentRepository" %>
<%@ page import="com.study.repository.FileRepository" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int boardId = Integer.parseInt(request.getParameter("boardId"));

    BoardRepository boardRepository = BoardRepository.getInstance();
    CommentRepository commentRepository = CommentRepository.getInstance();
    FileRepository fileRepository = FileRepository.getInstance();

    commentRepository.deleteCommentsByBoardId(boardId);
    fileRepository.deleteByBoardId(boardId);
    boardRepository.deleteById(boardId);

    response.sendRedirect("list.jsp");
%>