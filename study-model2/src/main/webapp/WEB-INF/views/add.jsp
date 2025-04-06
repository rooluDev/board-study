<%@ page import="java.util.List" %>
<%@ page import="com.study.DTO.CategoryDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<CategoryDTO> categoryList = (List<CategoryDTO>) request.getAttribute("categoryList");
    String pageNum = (String) request.getAttribute("pageNum");
    String startDate = (String) request.getAttribute("startDate");
    String endDate = (String) request.getAttribute("endDate");
    String categoryId = (String) request.getAttribute("categoryId");
    String searchText = (String) request.getAttribute("searchText");
%>
<html>
<head>
    <title>게시판 - 등록</title>
</head>
<body>
<h1>게시판 - 등록</h1>
<form method="post" action="/board?command=addProc" enctype="multipart/form-data" onsubmit="return validateForm()">
    <table>
        <tr>
            <select name="categoryId">
                <option value="-1">카테고리 선택</option>
                <%
                    for (CategoryDTO category : categoryList) {
                        out.println("<option value=\"" + category.getCategoryId() + "\">" + category.getCategoryName() + "</option>");
                    }
                %>
            </select>
        </tr>
        <tbody>
        <tr>
            <td><label>작성자</label></td>
            <td><input type="text" name="userName"></td>
        </tr>
        <tr>
            <td><label>비밀번호</label></td>
            <td><input type="password" id="password" name="password" placeholder="비밀번호" required></td>
            <td><input type="password" name="passwordCheck"></td>
        </tr>
        <tr>
            <td><label>제목</label></td>
            <td><input type="text" name="title"></td>
        </tr>
        <tr>
            <td><label>내용</label></td>
            <td><input type="text" name="content"></td>
        </tr>
        <tr>
            <td><label>파일첨부</label></td>
            <td><input type="file" name="file"></td>
            <td><input type="file" name="file"></td>
            <td><input type="file" name="file"></td>
        </tr>
        </tbody>
    </table>
    <button type="button" onclick="goToList()">취소</button>
    <input type="submit" value="저장">
</form>
</body>
<script>
    function goToList() {
        location.href = "/board?command=list&pageNum=<%=pageNum%>&startDate=<%=startDate%>&endDate=<%=endDate%>&categoryId=<%=categoryId%>&searchText=<%=searchText%>";
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
        if (!regex.test(password)) {
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
</html>
