<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%@page import="com.study.repository.BoardRepository" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.domain.Board" %>
<%@ page import="com.study.repository.CategoryRepository" %>

<%
    Date currentDate = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String formattedDate = dateFormat.format(currentDate);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(currentDate);
    calendar.add(Calendar.YEAR, -1);
    Date oneYearAgo = calendar.getTime();
    String formattedOneYearAgo = dateFormat.format(oneYearAgo);

    /* todo: 티 ㄹ 람유 */
    String pageNum = request.getParameter("pageNum");
    // 빈 문자열 처리
    if (pageNum == null) {
        pageNum = "1";
    }
    int currentPage = Integer.parseInt(pageNum);
    int pageSize = 10;
    int startRow = (currentPage - 1) * pageSize;
%>
<%
    BoardRepository boardRepository = new BoardRepository();
    CategoryRepository categoryRepository = new CategoryRepository();
    List<Board> boards = null;

    if(request.getParameter("searchText") != null){
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String category = request.getParameter("category");
        String searchText = request.getParameter("searchText");


        boards = boardRepository.findBySearch(startDate,endDate,category,searchText);
    }
    if(request.getParameter("searchText") == null) {
        boards = boardRepository.selectAll(pageSize, startRow);

    }
    int count = boards.size();
%>


<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>자유게시판 - 목록</h1>
<form action="search.jsp">
    <span>등록일 </span>
    <input type="date" id="startDate" name="startDate" value=<%=formattedOneYearAgo%>>

    <label for="endDate">~</label>
    <input type="date" id="endDate" name="endDate" value=<%=formattedDate%>>

    <select name="category">
        <option value="all">전체 카테고리</option>
        <option value="JAVA">JAVA</option>
        <option value="Javascript">Javascript</option>
        <option value="Database">Database</option>
    </select>
    <input type="text" name="searchText"id="search" placeholder="검색어를 입력해 주세요. (제목 + 작성자 + 내용)" required style="width: 300px"/>
    <input type="submit" value="검색">
</form>
<br/>
<div>총 <%=count%>건</div>
<%
    for (Board board : boards) {
%>
<%
    String categoryName = categoryRepository.findByBoardId(board.getBoardId());
%>
        <%=categoryName%>
<a href="board.jsp?boardId=<%=board.getBoardId()%>&pageNum=<%=pageNum%>"><%=board.getTitle()%><a/>
        <%=board.getUserName()%>
        <%=board.getCreatedAt()%>
        <%=board.getViews()%>
        <%=board.getCreatedAt()%>
        <%
            if(board.getEditedAt() != null){
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
        <%
            }
        %>
        <%
        int totalPages = boardRepository.getTotalPageCount();
        for (int i = 1; i <= totalPages; i++) {
    %>
    <a href="?pageNum=<%= i %>"><%= i %>
    </a>
        <%
        }
    %>
    <br/>
    <a href="posting.jsp">등록</a>
</body>
</html>
