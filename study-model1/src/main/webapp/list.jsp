<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.dto.Board" %>
<%@ page import="com.study.condition.SearchCondition" %>
<%@ page import="com.study.utils.ConditionUtils" %>
<%@ page import="com.study.dto.Category" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.study.utils.TimestampUtils" %>
<%@ page import="com.study.repository.BoardRepository" %>
<%@ page import="com.study.repository.CategoryRepository" %>
<%
    BoardRepository boardRepository = BoardRepository.getInstance();
    CategoryRepository categoryRepository = CategoryRepository.getInstance();

    List<Category> categoryList = categoryRepository.getCategoryList();
    List<Board> boardList = new ArrayList<>();

    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");
    String categoryId = request.getParameter("categoryId");
    String searchText = request.getParameter("searchText");
    String pageNum = request.getParameter("pageNum");

    SearchCondition searchCondition = ConditionUtils.parameterToSearchCondition(startDate, endDate, categoryId, searchText, pageNum);
    boardList = boardRepository.selectBoardList(searchCondition);

    int boardCount = boardRepository.selectRowCountForBoardList(searchCondition);
    int pageSize = 10;
    int totalPageNum = (int) Math.ceil((double) boardCount / pageSize);
%>

<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>자유게시판 - 목록</h1>
<form action="list.jsp">
    <span>등록일 </span>
    <input type="date" id="startDate" name="startDate" value=<%=searchCondition.getStartDate()%>>

    <label for="endDate">~</label>
    <input type="date" id="endDate" name="endDate" value=<%=searchCondition.getEndDate()%>>

    <select name="categoryId">
        <option value="-1">전체 카테고리</option>
        <%
            for (Category category : categoryList) {
                out.println("<option value=" + category.getCategoryId() + ">" + category.getCategoryName() + "</option>");
            }
        %>
    </select>
    <input type="text" name="searchText" id="search" placeholder="검색어를 입력해 주세요. (제목 + 작성자 + 내용)" required
           style="width: 300px"/>
    <input type="submit" value="검색">
</form>
<br/>
<h2>총 <%=boardCount%>건</h2>
<br/>
<table>
    <tr>
        <th scope="col">카테고리</th>
        <th scope="col"></th>
        <th scope="col">제목</th>
        <th scope="col">작성자</th>
        <th scope="col">조회수</th>
        <th scope="col">등록일시</th>
        <th scope="col">수정일시</th>
    </tr>
    <%
        for (Board board : boardList) {
    %>
    <tr>
        <td><%=board.getCategoryName()%>
        </td>
        <%
            if (board.getFileId() > 0) {
        %>
        <td>📁</td>
        <%
            }else {
        %>
        <td></td>
        <%
            }
        %>
        <td>
            <a href="board.jsp?boardId=<%=board.getBoardId()%>&startDate=<%=searchCondition.getStartDate()%>&endDate=<%=searchCondition.getEndDate()%>&categoryId=<%=searchCondition.getCategoryId()%>&searchText=<%=searchCondition.getSearchText()%>&pageNum=<%=searchCondition.getPageNum()%>">
                <%=board.getTitle()%>
            </a>
        </td>
        <td><%=board.getUserName()%>
        </td>
        <td><%=board.getViews()%>
        </td>
        <td><%=TimestampUtils.timestampToStringFormat(board.getCreatedAt(), "yyyy-MM-dd HH:mm:ss")%>
        </td>
        <%
            if (board.getEditedAt() != null) {
        %>
        <td><%=TimestampUtils.timestampToStringFormat(board.getEditedAt(), "yyyy-MM-dd HH:mm:ss")%>
        </td>
        <%
        } else {
        %>
        <td>-</td>
        <%
            }
        %>
    </tr>
    <%
        }
    %>
</table>
<%
    if (totalPageNum > 1) {
%>
    <%
        for (int pageIndex = 1; pageIndex <= totalPageNum; pageIndex++) {
    %>
<a href="list.jsp?&startDate=<%=searchCondition.getStartDate()%>&endDate=<%=searchCondition.getEndDate()%>&categoryId=<%=searchCondition.getCategoryId()%>&searchText=<%=searchCondition.getSearchText()%>&pageNum=<%=pageIndex%>"><%=pageIndex%></a>
    <%
        }
    %>
<%
    }
%>
<br/>
<a href="posting.jsp?&startDate=<%=searchCondition.getStartDate()%>&endDate=<%=searchCondition.getEndDate()%>&categoryId=<%=searchCondition.getCategoryId()%>&searchText=<%=searchCondition.getSearchText()%>&pageNum=<%=searchCondition.getPageNum()%>">등록</a>
</body>
</html>
