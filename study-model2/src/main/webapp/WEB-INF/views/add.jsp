<%@ page import="com.study.DAO.BoardDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.DTO.CategoryDTO" %><%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/23/24
  Time: 4:42 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<CategoryDTO> categoryList = (List<CategoryDTO>) request.getAttribute("categoryList");
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
            <select name="category">
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
    function goToList(){
        location.href = "/board?command=list";
    }
    function validateForm(){
        if (!validateCategory()){
            alert("카테고리를 선택하세요.");
            return false;
        }
        if (!validateUserName()){
            alert("작성자를 3글자 이상, 5글자 미만으로 입력하세요.");
            return false;
        }
        if (!validatePassword()){
            alert("비밀번호를 4글자 이상, 16글자 미만, 영문/숫자/특수문자를 포함하세요.");
            return false;
        }
        if (!validatePasswordMatch()){
            alert("비밀번호가 일치하지 않습니다.");
            return false;
        }
        if (!validateTitle()) {
            alert("제목을 4글자 이상, 100글자 미만으로 입력하세요.");
            return false;
        }
        if (!validateContent()) {
            alert("내용을 4글자 이상, 2000글자 미만으로 입력하세요.");
            return false;
        }
        return true;
    }

    function validateCategory(){
        const category = document.getElementById("category").value;
        if (category == "none") {
            return false;
        }
        return true;
    }
    function validateUserName(){
        const userName = document.getElementById("user_name").value;
        if (userName.length >= 3 && userName.length <5){
            return true;
        }
        return false;
    }
    function validatePassword(){
        const password = document.getElementById("password").value;
        const regex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{4,16}$/;
        return regex.test(password);
    }
    function validatePasswordMatch(){
        const password = document.getElementById("password").value;
        const passwordRe = document.getElementById("passwordRe").value;
        if (password == passwordRe){
            return true;
        }
        return false;
    }
    function validateTitle(){
        const title = document.getElementById("title").value;
        if (title.length >= 4 && title.length < 100) {
            return true;
        }
        return false;
    }
    function validateContent(){
        const content = document.getElementById("content").value;
        if (content.length >= 4 && content.length <= 2000) {
            return true;
        }
        return false;
    }
</script>
</html>
