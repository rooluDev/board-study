<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 1/15/24
  Time: 4:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
  <h1>게시판 - 등록</h1>
  <form action="saveData.jsp" method="post" onsubmit="return validateForm()">
      <label for="category">카테고리</label>
      <select name="category" id="category" required>
          <option value="none">카테고리 선택</option>
          <option value="JAVA">JAVA</option>
          <option value="Javascript">Javascript</option>
          <option value="Database">Database</option>
      </select>
      <br/>
      <label for="user_name">작성자</label>
      <input type="text" id="user_name" name="user_name" required>
      <br/>
      <label for="password">비밀번호</label>
      <input type="password" id="password" name="password" placeholder="비밀번호" required>
      <input type="password" id="passwordRe" name="passwordRe" placeholder="비밀번호 확인" required>
      <br/>
      <label for="title">제목</label>
      <input type="text" id="title" name="title" required>
      <br/>
      <label for="content">내용</label>
      <input type="text" id="content" name="content" required>
      <br/>
      <label for="file">파일 첨부</label>
      <div id="file">
          <input type="file" name="file1">
          <input type="file" name="file2">
          <input type="file" name="file3">
      </div>
      <br/>
      <button type="button" onclick="goToList()">취소</button>
      <button type="submit">저장</button>
  </form>
<script>
    function goToList(){
        const listPage = "list.jsp";
        location.href = listPage;
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
</body>
</html>
