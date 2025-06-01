<%@ page import="java.util.List" %>
<%@ page import="com.study.dto.Category" %>
<%@ page import="com.study.repository.CategoryRepository" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    CategoryRepository categoryRepository = CategoryRepository.getInstance();
    List<Category> categoryList = categoryRepository.getCategoryList();

    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");
    String categoryId = request.getParameter("categoryId");
    String searchText = request.getParameter("searchText");
    String pageNum = request.getParameter("pageNum");
%>
<html>
<head>
    <title>Posting</title>
</head>
<body>
<h1>게시판 - 등록</h1>
<form action="postingProc.jsp" method="post" onsubmit="return validateForm()" enctype="multipart/form-data">
    <label for="categoryId">카테고리</label>
    <select name="categoryId" id="categoryId">
        <option value="-1">전체 카테고리</option>
        <%
            for (Category category : categoryList) {
                out.println("<option value=" + category.getCategoryId() + ">" + category.getCategoryName() + "</option>");
            }
        %>
    </select>
    <br/>
    <label for="userName">작성자</label>
    <input type="text" name="userName" id="userName" required>
    <br/>
    <label for="password">비밀번호</label>
    <input type="password" id="password" name="password" placeholder="비밀번호" required>
    <input type="password" id="passwordRe" name="passwordRe" placeholder="비밀번호 확인" required>
    <br/>
    <label for="title">제목</label>
    <input type="text" name="title" id="title" required>
    <br/>
    <label for="content">내용</label>
    <input type="text" id="content" name="content" required>
    <br/>
    <label for="file">파일 첨부</label>
    <div id="file">
        <input type="file" name="file">
        <input type="file" name="file">
        <input type="file" name="file">
    </div>
    <br/>
    <button type="button" onclick="goToList()">취소</button>
    <button type="submit">저장</button>
</form>
<script>
    function goToList() {
        const listPage = "list.jsp";
        location.href = listPage + "?pageNum=<%=pageNum%>&startDate=<%=startDate%>&endDate=<%=endDate%>&categoryId=<%=categoryId%>&searchText=<%=searchText%>";
    }

    function validateForm() {
        const isValid = validateCategory() && validatePassword() && validateUserName() && validateTitle() && validateContent();
        return isValid;
    }

    function validateCategory() {
        const categoryId = document.getElementById("categoryId").value;

        if (categoryId === -1) {
            alert("카테고리를 선택하세요.");
            return false;
        }
        return true;
    }

    function validateUserName() {
        const userName = document.getElementById("userName").value;
        if (userName.length < 3 || userName.length > 4) {
            alert("작성자를 3글자 이상, 5글자 미만으로 입력하세요.");
            return false;
        }
        return true;
    }

    function validatePassword() {
        const password = document.getElementById("password").value;

        const regex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{4,16}$/;
        if (!regex.test(password)){
            alert("비밀번호를 4글자 이상, 16글자 미만, 영문/숫자/특수문자를 포함하세요.");
            return false;
        }
        return true;
    }

    function validatePasswordMatch() {
        const password = document.getElementById("password").value;
        const passwordRe = document.getElementById("passwordRe").value;
        if (password !== passwordRe) {
            alert("비밀번호가 일치하지 않습니다.");
            return false;
        }
        return true;
    }

    function validateTitle() {
        const title = document.getElementById("title").value;
        if (title.length < 4 || title.length > 100) {
            alert("제목을 4글자 이상, 100글자 미만으로 입력하세요.");
            return false;
        }
        return true;
    }

    function validateContent() {
        const content = document.getElementById("content").value;
        if (content.length < 4 || content.length > 2000) {
            alert("내용을 4글자 이상, 2000글자 미만으로 입력하세요.");
            return false;
        }
        return true;
    }
</script>
</body>
</html>
