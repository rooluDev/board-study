<%@ page import="java.util.List" %>
<%@ page import="com.study.DTO.CategoryDTO" %>
<%@ page import="com.study.condition.SearchCondition" %>
<%@ page import="com.study.DTO.BoardDTO" %>
<%@ page import="com.study.utils.TimestampUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    SearchCondition searchCondition = (SearchCondition) request.getAttribute("searchCondition");
    List<CategoryDTO> categoryList = (List<CategoryDTO>) request.getAttribute("categoryList");
    List<BoardDTO> boardList = (List<BoardDTO>) request.getAttribute("boardList");
    int totalPageNum = (Integer) request.getAttribute("totalPageNum");
    int countBoard = (Integer) request.getAttribute("countBoard");
%>

<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>자유 게시판 - 목록</h1>
<form method="post" name="search" action="/board?command=list">
    <input type="date" name="startDate" value="<%=searchCondition.getStartDate()%>">
    <input type="date" name="endDate" value="<%=searchCondition.getEndDate()%>">
    <select name="categoryId">
        <option value="-1">전체 카테고리</option>
        <%
            for (CategoryDTO category : categoryList) {
                out.println("<option value=" + category.getCategoryId() + ">" + category.getCategoryName() + "</option>");
            }
        %>
        <input type="text" name="searchText" id="search" value="<%=searchCondition.getSearchText()%>"
               placeholder="검색어를 입력해 주세요. (제목 + 작성자 + 내용)" required
               style="width: 300px"/>
        <input type="submit" value="검색">
    </select>
    <br/>
    <h2>총 게시물 수: <%=countBoard%>
    </h2>
    <table>
        <thead>
        </thead>
        <th>카테고리</th>
        <th scope="col"></th>
        <th>제목</th>
        <th>작성자</th>
        <th>조회수</th>
        <th>등록일시</th>
        <th>수정일시</th>
        <tbody>
        <%
            for (BoardDTO board : boardList) {
        %>
        <tr>
            <td>
                <%=board.getCategoryName()%>
            </td>
            <%
                if (board.getFileId() > 0) {
            %>
            <td>📁</td>
            <%
            } else {
            %>
            <td></td>
            <%
                }
            %>
            <td>
                <a href="/board?command=view&boardId=<%=board.getBoardId()%>&startDate=<%=searchCondition.getStartDate()%>&endDate=<%=searchCondition.getEndDate()%>&categoryId=<%=searchCondition.getCategoryId()%>&searchText=<%=searchCondition.getSearchText()%>&pageNum=<%=searchCondition.getPageNum()%>">
                    <%=board.getTitle()%>
                </a>
            </td>
            <td><%= board.getUserName()%>
            </td>
            <td><%= board.getViews()%>
            </td>
            <td><%= TimestampUtils.parseToString(board.getCreatedAt(), "yyyy.MM.dd hh:dd")%>
            </td>
            <td>
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
            </td>
        </tr>
        <%}%>
        </tbody>
    </table>

    <%
        for (int pageNum = 1; pageNum <= totalPageNum; pageNum++) {
    %>
    <a href="board?command=list&pageNum=<%=pageNum%>&startDate=<%=searchCondition.getStartDate()%>&endDate=<%=searchCondition.getEndDate()%>&categoryId=<%=searchCondition.getCategoryId()%>&searchText=<%=searchCondition.getSearchText()%>"><%=pageNum%>
    </a>
    <%
        }
    %>
    <br/>
    <button><a
            href="/board?command=add&startDate=<%=searchCondition.getStartDate()%>&endDate=<%=searchCondition.getEndDate()%>&categoryId=<%=searchCondition.getCategoryId()%>&searchText=<%=searchCondition.getSearchText()%>&pageNum=<%=searchCondition.getPageNum()%>">글쓰기</a>
    </button>
</form>
</body>
</html>
