# 📋 Medel 1

## 📝 프로젝트 개요

이 프로젝트는 **Model1 (JSP)** 구조를 활용해 게시판을 구현한 학습용 프로젝트입니다.  
게시판의 기본 기능인 **글 작성, 수정, 삭제, 검색, 파일 업로드** 등을 직접 구현하여  
웹 애플리케이션의 기본 아키텍처를 이해하고 JSP 기반 개발 역량을 강화하는 것을 목표로 했습니다.


## 💡 주요 기능

### 1️⃣ 게시판 작성

사용자가 제목, 내용, 파일을 업로드하여 게시글을 작성할 수 있는 기능입니다.  
파일은 UUID 기반으로 서버에 저장되며, 메타데이터가 DB에 기록됩니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>
  
    postingProc.jsp
     
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
  </details>

### 2️⃣ 게시판 수정

작성자가 비밀번호를 입력해 본인 글을 수정할 수 있는 기능입니다.
첨부 파일을 개별 삭제하거나 새 파일을 추가할 수 있으며, 수정된 내용은 DB에 반영됩니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>
  
    editProc.jsp
     
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
       
  </details>
  
### 3️⃣ 게시판 삭제

게시글과 연관된 댓글 및 첨부 파일을 함께 삭제하는 기능입니다.
비밀번호 검증 후 모든 관련 데이터를 안전하게 제거합니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>
  
    delete.jsp
     
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
    
  </details>
  
### 4️⃣ 게시판 수정

게시글 목록을 조회할 때 날짜, 카테고리, 검색어 조건으로 필터링할 수 있습니다.
조회 시 조회수가 증가하며, 댓글과 첨부 파일 데이터를 함께 가져옵니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>
  
    add.jsp
     
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
     
     
    SearchCondition Util
     
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

  </details>
    
## 🛠 기술 스택
![Java](https://img.shields.io/badge/Java-E76F00?style=for-the-badge&logo=java&logoColor=white)
![JSP](https://img.shields.io/badge/JSP-F57C00?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-00618A?style=for-the-badge&logo=mysql&logoColor=white)
