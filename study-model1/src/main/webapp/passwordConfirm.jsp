<%@ page import="com.study.repository.BoardRepository" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String inputPassword = request.getParameter("inputPassword");
    String location = request.getParameter("location");
    int boardId = Integer.parseInt(request.getParameter("boardId"));
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");
    String categoryId = request.getParameter("categoryId");
    String searchText = request.getParameter("searchText");
    String pageNum = request.getParameter("pageNum");

    BoardRepository boardRepository = BoardRepository.getInstance();

    boolean isExist = boardRepository.selectBoardByPassword(boardId, inputPassword) == 1;

    if (isExist) {
        response.sendRedirect(location + "?boardId=" + boardId + "&startDate=" + startDate + "&endDate=" + endDate +
                "&categoryId=" + categoryId + "&searchText=" + searchText + "&pageNum=" + pageNum);
    } else {
%>
<script>
    alert("비밀번호가 일치하지 않습니다.");
    history.back();
</script>
<%
    }
%>