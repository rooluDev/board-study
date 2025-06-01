<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.study.validate.BoardValidator" %>
<%@ page import="org.apache.commons.fileupload.*" %>
<%@ page import="org.apache.commons.fileupload.servlet.*" %>
<%@ page import="org.apache.commons.fileupload.disk.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.study.dto.Board" %>
<%@ page import="com.study.dto.File" %>
<%@ page import="java.util.UUID" %>
<%@ page import="org.apache.commons.io.FilenameUtils" %>
<%@ page import="com.study.repository.BoardRepository" %>
<%@ page import="com.study.repository.FileRepository" %>
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