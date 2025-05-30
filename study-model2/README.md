# 📋 Medel 2

## 📝 프로젝트 개요
이 프로젝트는 게시판을 Modle2구조(JSP와 Servlet)를 활용해 구축하는 것을 목표로 합니다.

게시판의 주 기능인 글 작성, 수정, 삭제 등 을 구현했습니다.

## 💡 주요 기능
+ 게시판 작성
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
     
  
+ 게시판 수정
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
     
  
+ 게시판 삭제
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
     
  
+ 게시판 검색
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
+ Command, Factory Pattern
  <details>
   <summary>코드 보기(펼치기/접기)</summary>

  요청 URL의 쿼리스트링 파라미터(command) 값을 기반으로 FrontController에서 요청의 종류를 식별하고, 해당 요청에 대응하는 Command 객체를 Factory 패턴을 통해 생성합니다. 이를 통해 요청 처리 로직을 각각의 Command 클래스로 모듈화하고, 컨트롤러의 역할은 단순히 명령 분기 및 실행으로 축소하여 유지보수성과 확장성을 높였습니다. 대표적인 Command 패턴 + Factory 패턴 조합으로, 다형성을 활용한 유연한 요청 처리 구조를 구현한 사례입니다.

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

## 🛠 기술 스택
![Java](https://img.shields.io/badge/Java-E76F00?style=for-the-badge&logo=java&logoColor=white)
![JSP](https://img.shields.io/badge/JSP-F57C00?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-00618A?style=for-the-badge&logo=mysql&logoColor=white)
