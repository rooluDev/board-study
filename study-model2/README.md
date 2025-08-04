# 📋 Medel 2

## 📝 프로젝트 개요
이 프로젝트는 **Model2 (JSP & Servlet)** 구조를 활용해 게시판을 구현한 학습용 프로젝트입니다.  
**Front Controller + Command + Factory 패턴**을 적용하여, 요청 처리 로직을 모듈화하고  
유지보수성과 확장성을 높이는 것을 목표로 했습니다.  
게시판의 기본 기능(글 작성, 수정, 삭제, 검색, 파일 업로드)을 구현했습니다.

## 💡 주요 기능

### 1️⃣ 게시판 작성

사용자가 제목, 내용, 파일을 업로드하여 게시글을 작성할 수 있는 기능입니다.  
파일은 UUID 기반으로 서버에 저장되며, 메타데이터가 DB에 기록됩니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>
  
      AddProcCommand
      ```
      public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            response.sendRedirect("/error.jsp");
        }

        FileProperties fileProperties = new FileProperties();
        Map<String, String> propertiesMap = fileProperties.getProperties();
        String path = propertiesMap.get("filePath");
        String characterSet = propertiesMap.get("characterSet");

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
        List<FileDTO> fileList = new ArrayList<>();

        for (FileItem item : fileItemList) {
            String fieldName = item.getFieldName();
            String fieldValue = item.getString(characterSet);
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
            } else {
                boolean fileNullCheck = item == null || item.getName().isEmpty();
                if (!fileNullCheck) {
                    int size = (int) item.getSize();
                    String physicalName = UUID.randomUUID().toString();
                    String fileName = item.getName();
                    String extension = FilenameUtils.getExtension(fileName);

                    FileDTO file = FileDTO.builder()
                            .physicalName(physicalName)
                            .originalName(fileName)
                            .extension(extension)
                            .size(size)
                            .filePath(path)
                            .build();

                    fileList.add(file);

                    String filePath = path + "/" + physicalName + "." + extension;
                    java.io.File uploadedFile = new java.io.File(filePath);
                    item.write(uploadedFile);
                }
            }
        }

        BoardDTO board = BoardDTO.builder()
                .userName(userName)
                .categoryId(categoryId)
                .password(password)
                .passwordRe(passwordRe)
                .title(title)
                .content(content)
                .build();

        if (!BoardFormValidate.validateBoardForPost(board)) {
            response.sendRedirect("/error.jsp");
        }


        BoardDAO boardDAO = new BoardDAO();
        int boardId = boardDAO.insertBoard(board);
        FileDAO fileDAO = new FileDAO();
        fileList.forEach(file -> {
            file.setBoardId(boardId);
            try {
                fileDAO.insertFile(file);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        response.sendRedirect("/board?command=list");
    }
    
      ```
  </details>
  
### 2️⃣ 게시판 수정

작성자가 비밀번호를 입력해 본인 글을 수정할 수 있는 기능입니다.
첨부 파일을 개별 삭제하거나 새 파일을 추가할 수 있으며, 수정된 내용은 DB에 반영됩니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>
  
    edit.jsp
    ```
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int boardId = Integer.parseInt(request.getParameter("boardId"));

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            response.sendRedirect("/error.jsp");
        }

        FileProperties fileProperties = new FileProperties();
        Map<String, String> propertiesMap = fileProperties.getProperties();
        String path = propertiesMap.get("filePath");
        String characterSet = propertiesMap.get("characterSet");

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
        List<FileDTO> newFileList = new ArrayList<>();

        for (FileItem item : fileItemList) {
            // Text Field
            String fieldName = item.getFieldName();
            String fieldValue = item.getString(characterSet);
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

                    FileDTO file = FileDTO.builder()
                            .physicalName(physicalName)
                            .originalName(fileName)
                            .extension(extension)
                            .size(size)
                            .filePath("/Users/user/upload")
                            .build();

                    newFileList.add(file);

                    String filePath = path + "/" + physicalName + "." + extension;
                    java.io.File uploadedFile = new java.io.File(filePath);
                    item.write(uploadedFile);
                }
            }
        }
        BoardDAO boardDAO = new BoardDAO();
        FileDAO fileDAO = new FileDAO();

        if (boardDAO.selectBoardByPassword(boardId, password) != 1) {
            response.sendRedirect("/error.jsp");
        }

        BoardDTO board = BoardDTO.builder()
                .boardId(boardId)
                .userName(userName)
                .title(title)
                .content(content)
                .build();

        if(!BoardFormValidate.validateBoardForEdit(board)) {
            response.sendRedirect("/error.jsp");
        }

        boardDAO.updateBoard(board);

        for (int fileId : deleteFileIdList) {
            fileDAO.deleteById(fileId);
        }

        for (FileDTO fileDTO : newFileList) {
            fileDTO.setBoardId(boardId);
            fileDAO.insertFile(fileDTO);
        }

        response.sendRedirect("list.jsp");
    }  
    ```
  </details>

### 3️⃣ 게시판 삭제

게시글과 연관된 댓글 및 첨부 파일을 함께 삭제하는 기능입니다.
비밀번호 검증 후 모든 관련 데이터를 안전하게 제거합니다.
  <details>
   <summary>코드 보기(펼치기/접기)</summary>
  
    DeleteCommand
    ```
     public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int boardId = Integer.parseInt(request.getParameter("boardId"));

        BoardDAO boardDAO = new BoardDAO();
        CommentDAO commentDAO = new CommentDAO();
        FileDAO fileDAO = new FileDAO();

        commentDAO.deleteCommentsByBoardId(boardId);
        fileDAO.deleteByBoardId(boardId);
        boardDAO.deleteById(boardId);

        response.sendRedirect("/board?command=list");
    }
    ```
  </details>
  
### 4️⃣ 게시판 검색

게시글 목록을 날짜, 카테고리, 검색어로 필터링할 수 있으며, 페이지네이션을 지원합니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>
  
    ListCommand
    ```
     public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = "/WEB-INF/views/list.jsp";

        String pageNum = request.getParameter("pageNum");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String categoryId = request.getParameter("categoryId");
        String searchText = request.getParameter("searchText");

        SearchCondition searchCondition = ConditionUtils.parameterToSearchCondition(startDate, endDate, categoryId, searchText, pageNum);

        BoardDAO boardDAO = new BoardDAO();
        CategoryDAO categoryDAO = new CategoryDAO();

        int pageSize = 10;
        int countBoard = boardDAO.selectRowCountForBoardList(searchCondition);
        int totalPageNum = (int) Math.ceil((double) countBoard / (double) pageSize);

        List<CategoryDTO> categoryList = categoryDAO.getCategoryList();
        List<BoardDTO> boardList = boardDAO.selectBoardList(searchCondition);

        request.setAttribute("searchCondition", searchCondition);
        request.setAttribute("categoryList", categoryList);
        request.setAttribute("boardList", boardList);
        request.setAttribute("totalPageNum", totalPageNum);
        request.setAttribute("countBoard", countBoard);

        request.getRequestDispatcher(path).forward(request, response);
    }
  ```

  </details>

### 5️⃣ Command, Factory Pattern

Front Controller가 요청 파라미터(command)를 읽어 해당하는 Command 객체를 Factory를 통해 생성합니다.
이를 통해 요청 처리 로직을 Command 단위로 분리하여 유연한 구조와 확장성을 확보했습니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>

    Controller
    ```
    /**
     * GET 요청 Controller
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String cmd = request.getParameter("command");

        if (cmd == null) {
            cmd = "list";
        }

        Command command = this.createCommand(cmd);

        if (command == null) {
            response.sendRedirect("/error");
        }
        try {
            command.execute(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * POST 요청 Controller
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        String cmd = request.getParameter("command");
        Command command = this.createCommand(cmd);
        try {
            command.execute(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  ```
  Command Facotry
  ```
    /**
     * Command에 맞는 Command 생성
     *
     * @param cmd command
     * @return Command 객체
     */
    public Command createCommand(String cmd) {
        switch (cmd) {
            case "list":
                return new ListCommand();
            case "add":
                return new AddCommand();
            case "addProc":
                return new AddProcCommand();
            case "view":
                return new ViewCommand();
            case "edit":
                return new EditCommand();
            case "delete":
                return new DeleteCommand();
            case "commentProc":
                return new CommentProc();
            case "download":
                return new DownloadCommand();
            case "error":
                return new ErrorCommand();
            case "passwordConfirm":
                return new PasswordConfirmCommand();
            case "editProc":
                return new EditProcCommand();
            default:
                return null;
        }
    }
  ```
</details>

## 🛠 기술 스택
![Java](https://img.shields.io/badge/Java-E76F00?style=for-the-badge&logo=java&logoColor=white)
![JSP](https://img.shields.io/badge/JSP-F57C00?style=for-the-badge)
![MyBatis](https://img.shields.io/badge/MyBatis-555555?style=for-the-badge&logo=MyBatis&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00618A?style=for-the-badge&logo=mysql&logoColor=white)
