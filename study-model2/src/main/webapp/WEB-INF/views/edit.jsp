<%@ page import="com.study.DTO.BoardDTO" %>
<%@ page import="com.study.DTO.FileDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.utils.TimestampUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    BoardDTO board = (BoardDTO) request.getAttribute("board");
    List<FileDTO> fileList = (List<FileDTO>) request.getAttribute("fileList");
    String pageNum = (String) request.getAttribute("pageNum");
    String startDate = (String) request.getAttribute("startDate");
    String endDate = (String) request.getAttribute("endDate");
    String categoryId = (String) request.getAttribute("categoryId");
    String searchText = (String) request.getAttribute("searchText");
%>
<html>
<head>
    <title>Title</title>
</head>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<body>
<%=board.getCategoryName()%>
</br>
<%=TimestampUtils.parseToString(board.getCreatedAt(), "yyyy.MM.dd hh:dd")%>
</br>
<%
    if (board.getEditedAt() != null) {
%>
<%=TimestampUtils.parseToString(board.getEditedAt(), "yyyy.MM.dd hh:dd")%>
<%
} else {
%>
<span>-</span>
<%
    }
%>
<br/>
<span>조회수</span><%=board.getViews()%>
</br>
<form action="/board?command=editProc&boardId=<%=board.getBoardId()%>" onsubmit="return validateForm()" method="post"
      enctype="multipart/form-data">
    <label for="user_name">작성자</label>
    <input type="text" id="user_name" name="userName" value="<%=board.getUserName()%>" placeholder="작성자">
    <br/>
    <label for="password">비밀번호</label>
    <input type="password" id="password" name="password" placeholder="비밀번호">
    <br/>
    <label for="title">제목</label>
    <input type="text" id="title" name="title" value="<%=board.getTitle()%>">
    <br/>
    <label for="content">내용</label>
    <input type="text" id="content" name="content" value="<%=board.getContent()%>">
    <br/>
    <span>첨부파일</span>
    <%
        for (FileDTO file : fileList) {
    %>
    <div id="fileBox<%=file.getFileId()%>">
        <span><%=file.getOriginalName()%></span>
        <button type="button" onclick="deleteFile(<%=file.getFileId()%>)">X</button>
    </div>
    <%
        }
    %>
    <div id="newFiles"></div>
    <button type="button" onclick="addFileInput()">첨부파일 추가</button>
    <br/>
    <input type="hidden" id="deleteFileId" name="deleteFileId">
    <button type="button" onclick="goToBoard()">취소</button>
    <button type="submit">저장</button>
</form>

</body>
<script>
    let existingFileCount = <%= fileList.size() %>;
    let newFileCount = 0;
    let maxFiles = 3;

    function validateForm() {
        const isValid = validateUserName() && validateTitle() && validateContent();
        return isValid;
    }

    function validateUserName() {
        const userName = document.getElementById("user_name").value;
        if (userName.length < 3 || userName.length > 4) {
            alert("작성자를 3글자 이상, 5글자 미만으로 입력하세요.");
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

    function deleteFile(fileId) {

        const deleteFileIdElement = document.getElementById("deleteFileId");

        const fileBoxElement = document.getElementById("fileBox" + fileId);
        if (fileBoxElement) {
            fileBoxElement.remove();

        }
        if (deleteFileIdElement.value === "") {
            deleteFileIdElement.value = fileId;
            existingFileCount--;
        } else {
            deleteFileIdElement.value += "," + fileId;
        }

    }

    function addFileInput() {
        if (existingFileCount + newFileCount >= maxFiles) {
            alert("첨부파일은 최대 " + maxFiles + "개까지 가능합니다.");
            return;
        }
        newFileCount++;
        const newFilesContainer = document.getElementById("newFiles");
        const input = document.createElement("input");
        input.type = "file";
        input.name = "newFile";
        newFilesContainer.appendChild(input);
        newFilesContainer.appendChild(document.createElement("br"));
    }

    function goToBoard() {
        location.href = "board.jsp?boardId=<%=board.getBoardId()%>&pageNum=<%=pageNum%>&startDate=<%=startDate%>&endDate=<%=endDate%>&categoryId=<%=categoryId%>&searchText=<%=searchText%>";
    }
</script>
</html>
