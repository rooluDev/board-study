# 📋 Medel 1

## 📝 프로젝트 개요
이 프로젝트는 게시판을 Model1(JSP)구조를 활용해 구축하는 것을 목표로 합니다.

게시판의 주 기능인 글 작성, 수정, 삭제 등 을 구현했습니다.

## 💡 주요 기능
+ 게시판 작성
  <details>
   <summary>코드 보기(펼치기/접기)</summary>
  
    postingProc.jsp
     ```
      <%
          BoardRepository boardRepository = BoardRepository.getInstance();
          FileRepository fileRepository = FileRepository.getInstance();
      
          boolean isMultipart = ServletFileUpload.isMultipartContent(request);
      
          if (!isMultipart) {
              response.sendRedirect("posting.jsp");
          }
      
          DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
          ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
          servletFileUpload.setHeaderEncoding("UTF-8");
          servletFileUpload.setSizeMax(2 * 1024 * 1024);
      
          List<FileItem> fileItemList = servletFileUpload.parseRequest(request);
      
      
          int categoryId = 0;
          String userName = null;
          String password = null;
          String passwordRe = null;
          String title = null;
          String content = null;
          List<File> fileList = new ArrayList<>();
      
          for (FileItem item : fileItemList) {
              String fieldName = item.getFieldName();
              String fieldValue = item.getString("UTF-8");
      
              // Text Field
              if (item.isFormField()) {
                  if ("categoryId".equals(fieldName)) {
                      categoryId = Integer.parseInt(fieldValue);
                  } else if ("userName".equals(fieldName)) {
                      userName = fieldValue;
                  } else if ("password".equals(fieldName)) {
                      password = fieldValue;
                  } else if ("passwordRe".equals(fieldName)) {
                      passwordRe = fieldValue;
                  } else if ("title".equals(fieldName)) {
                      title = fieldValue;
                  } else if ("content".equals(fieldName)) {
                      content = fieldValue;
                  }
              }
              // File Field
              else {
                  boolean fileNullCheck = item == null || item.getName().isEmpty();
                  if (!fileNullCheck) {
                      int size = (int) item.getSize();
                      String physicalName = UUID.randomUUID().toString();
                      String fileName = item.getName();
                      String extension = FilenameUtils.getExtension(fileName);
      
                      File file = File.builder()
                              .physicalName(physicalName)
                              .originalName(fileName)
                              .extension(extension)
                              .size(size)
                              .filePath("/Users/user/upload")
                              .build();
      
                      fileList.add(file);
      
                      String filePath = "/Users/user/upload/" + physicalName + "." + extension;
                      java.io.File uploadedFile = new java.io.File(filePath);
                      item.write(uploadedFile);
                  }
              }
          }
      
          Board board = Board.builder()
                  .userName(userName)
                  .categoryId(categoryId)
                  .password(password)
                  .passwordRe(passwordRe)
                  .title(title)
                  .content(content)
                  .build();
      
          if (!BoardValidator.validateBoardForPost(board)) {
              response.sendRedirect("error.jsp");
          }
      
          int boardId = boardRepository.insertBoard(board);
          for (File file : fileList) {
              file.setBoardId(boardId);
              fileRepository.insertFile(file);
          }
      
      
          response.sendRedirect("list.jsp");
      %>
     ```
  
+ 게시판 수정
  <details>
   <summary>코드 보기(펼치기/접기)</summary>
  
    editProc.jsp
     ```
      <%
          BoardRepository boardRepository = BoardRepository.getInstance();
          FileRepository fileRepository = FileRepository.getInstance();
          int boardId = Integer.parseInt(request.getParameter("boardId"));
      
          boolean isMultipart = ServletFileUpload.isMultipartContent(request);
      
          if (!isMultipart) {
              response.sendRedirect("list.jsp");
          }
      
          DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
          ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
          servletFileUpload.setHeaderEncoding("UTF-8");
          servletFileUpload.setSizeMax(2 * 1024 * 1024);
      
          List<FileItem> fileItemList = servletFileUpload.parseRequest(request);
      
          String userName = null;
          String password = null;
          String title = null;
          String content = null;
          List<Integer> deleteFileIdList = new ArrayList<>();
          List<File> newFileList = new ArrayList<>();
      
          for (FileItem item : fileItemList) {
              // Text Field
              String fieldName = item.getFieldName();
              String fieldValue = item.getString("UTF-8");
              if (item.isFormField()) {
                  if ("userName".equals(fieldName)) {
                      userName = fieldValue;
                  } else if ("password".equals(fieldName)) {
                      password = fieldValue;
                  } else if ("title".equals(fieldName)) {
                      title = fieldValue;
                  } else if ("content".equals(fieldName)) {
                      content = fieldValue;
                  } else if ("deleteFileId".equals(fieldName)) {
                      if (fieldValue != null && !fieldValue.trim().isEmpty()) {
                          String[] fileIdArray = fieldValue.split(",");
                          for (String idStr : fileIdArray) {
                              int fileId = Integer.parseInt(idStr.trim());
                              deleteFileIdList.add(fileId);
                          }
                      }
                  }
              } else {
                  boolean fileNullCheck = item == null || item.getName().isEmpty();
                  if (!fileNullCheck) {
                      int size = (int) item.getSize();
                      String physicalName = UUID.randomUUID().toString();
                      String fileName = item.getName();
                      String extension = FilenameUtils.getExtension(fileName);
      
                      File file = File.builder()
                              .physicalName(physicalName)
                              .originalName(fileName)
                              .extension(extension)
                              .size(size)
                              .filePath("/Users/user/upload")
                              .build();
      
                      newFileList.add(file);
      
                      String filePath = "/Users/user/upload/" + physicalName + "." + extension;
                      java.io.File uploadedFile = new java.io.File(filePath);
                      item.write(uploadedFile);
                  }
              }
          }
          if (boardRepository.selectBoardByPassword(boardId, password) != 1) {
              response.sendRedirect("error.jsp");
          }
      
          Board board = Board.builder()
                  .boardId(boardId)
                  .userName(userName)
                  .title(title)
                  .content(content)
                  .build();
      
          if (!BoardValidator.validateBoardForEdit(board)) {
              response.sendRedirect("error.jsp");
          }
      
          boardRepository.updateBoard(board);
      
          for (int fileId : deleteFileIdList) {
              fileRepository.deleteById(fileId);
          }
      
          for (File file : newFileList) {
              file.setBoardId(boardId);
              fileRepository.insertFile(file);
          }
      
          response.sendRedirect("list.jsp");
      %>
    
     ```
  
+ 게시판 삭제
  <details>
   <summary>코드 보기(펼치기/접기)</summary>
  
    delete.jsp
     ```
    <%
        int boardId = Integer.parseInt(request.getParameter("boardId"));
    
        BoardRepository boardRepository = BoardRepository.getInstance();
        CommentRepository commentRepository = CommentRepository.getInstance();
        FileRepository fileRepository = FileRepository.getInstance();
    
        commentRepository.deleteCommentsByBoardId(boardId);
        fileRepository.deleteByBoardId(boardId);
        boardRepository.deleteById(boardId);
    
        response.sendRedirect("list.jsp");
    %>
    
     ```
  
+ 게시판 검색
  <details>
   <summary>코드 보기(펼치기/접기)</summary>
  
    add.jsp
     ```
      <%
          int boardId = Integer.parseInt(request.getParameter("boardId"));
          String startDate = request.getParameter("startDate");
          String endDate = request.getParameter("endDate");
          String categoryId = request.getParameter("categoryId");
          String searchText = request.getParameter("searchText");
          String pageNum = request.getParameter("pageNum");
      
          BoardRepository boardRepository = BoardRepository.getInstance();
          Board board = boardRepository.selectBoardById(boardId);
          boardRepository.plusViews(boardId);
      
          CommentRepository commentRepository = CommentRepository.getInstance();
          List<Comment> commentList = commentRepository.selectCommentListByBoardId(boardId);
      
          FileRepository fileRepository = FileRepository.getInstance();
          List<File> fileList = fileRepository.selectFileListByBoardId(boardId);
      %>
      <html>
      <head>
          <title>Title</title>
      </head>

     ...
     
     ```
    SearchCondition Util
     ```
    /**
     * 검색조건이 없는 경우와 검색조건이 있는 경우에 맞는 SearchCondition 반환
     *
     * @param startDate  처음 검색 기간
     * @param endDate    끝 검색 기간
     * @param categoryId 카테고리
     * @param searchText 검색어
     * @param pageNum    페이지
     * @return 검색조건 객체
     */
    public static SearchCondition parameterToSearchCondition(String startDate, String endDate, String categoryId, String searchText, String pageNum) {
        boolean isInit = startDate == null || endDate == null || categoryId == null || searchText == null;
        if (isInit) {
            return new SearchCondition();
        } else {
            if (pageNum == null) {
                pageNum = "1";
            }
            return new SearchCondition(startDate, endDate, categoryId, searchText, pageNum);
        }
    }
     ```
     
## 🛠 기술 스택
![Java](https://img.shields.io/badge/Java-E76F00?style=for-the-badge&logo=java&logoColor=white)
![JSP](https://img.shields.io/badge/JSP-F57C00?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-00618A?style=for-the-badge&logo=mysql&logoColor=white)
