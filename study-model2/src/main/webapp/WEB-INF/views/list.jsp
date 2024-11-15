<%@ page import="com.study.DAO.CategoryDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.DTO.CategoryDTO" %>
<%@ page import="com.study.condition.SearchCondition" %>
<%@ page import="com.study.DTO.BoardDTO" %>
<%@ page import="com.study.utils.TimestampUtils" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/22/24
  Time: 8:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    CategoryDAO categoryDAO = new CategoryDAO();
    SearchCondition searchCondition = (SearchCondition) request.getAttribute("searchCondition");
    List<BoardDTO> boardList = (List<BoardDTO>) request.getAttribute("boardList");
    List<CategoryDTO> categoryList= (List<CategoryDTO>)request.getAttribute("categoryList");
    int pageNum = (Integer) request.getAttribute("pageNum");
    int countBoard = (Integer) request.getAttribute("countBoard");
    int totalPageNum = (Integer) request.getAttribute("totalPageNum");
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>자유 게시판 - 목록</h1>
<form method="post" name="search" action="/board?command=list">
    <input type="date" name="startDate" value="<%=TimestampUtils.parseToString(searchCondition.getStartDate(),"yyyy-MM-dd")%>">
    <input type="date" name="endDate" value="<%=TimestampUtils.parseToString(searchCondition.getEndDate(),"yyyy-MM-dd")%>">
    <select name="category">
        <option value="-1">전체 카테고리</option>
        <%
            for (CategoryDTO category : categoryList) {
                out.println("<option value=" + category.getCategoryId() + ">" + category.getCategoryName() + "</option>");
            }
        %>
        <input type="text" name="searchText" id="search" placeholder="검색어를 입력해 주세요. (제목 + 작성자 + 내용)" required
               style="width: 300px"/>
        <input type="submit" value="검색">
    </select>
    <br/>
    <h2>총 게시물 수: <%=countBoard%></h2>
    <table>
        <thead>

        </thead>
        <th>카테고리</th>
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
            <td><%= categoryDAO.findById(board.getCategoryId())%>
            </td>
            <td><a href="/board?command=view&boardId=<%=board.getBoardId()%>&pageNum=<%=pageNum%>"><%= board.getTitle()%>
            <td/>
            </td>
            <td><%= board.getUserName()%>
            </td>
            <td><%= board.getViews()%>
            </td>
            <td><%= TimestampUtils.parseToString(board.getCreatedAt(),"yyyy.MM.dd hh:dd")%>
            </td>
            <td>
                <%
                    if(board.getEditedAt() != null){
                %>
                <%= TimestampUtils.parseToString(board.getEditedAt(),"yyyy.MM.dd hh:dd")%>
                <%
                    }else{
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
        for (int i = 1; i <= totalPageNum; i++) {
    %>
    <a href="board?command=list&pageNum=<%=i%>"><%=i%></a>
    <%
        }
    %>
    <br/>

    <button><a href="/board?command=add">글쓰기</a></button>
</form>
</body>
</html>
