<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory" %>
<%@ page import="org.apache.commons.fileupload.FileItem" %>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.service.BoardService" %>
<%@ page import="com.study.service.FileService" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.study.dto.Board" %>
<%@ page import="java.util.UUID" %>
<%@ page import="org.apache.commons.io.FilenameUtils" %>
<%@ page import="com.study.dto.File" %>
<%@ page import="com.study.validate.BoardValidator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    BoardService boardService = new BoardService();
    FileService fileService = new FileService();
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
    if(!boardService.findBoardByPasswordAndId(boardId, password)){
        response.sendRedirect("error.jsp");
    }

    Board board = Board.builder()
            .boardId(boardId)
            .userName(userName)
            .title(title)
            .content(content)
            .build();

    if(!BoardValidator.validateBoardForEdit(board)){
        response.sendRedirect("error.jsp");
    }

    boardService.editBoard(board);

    for (int fileId : deleteFileIdList) {
        fileService.deleteById(fileId);
    }

    for (File file : newFileList) {
        file.setBoardId(boardId);
    }

    fileService.addFileList(newFileList);

    response.sendRedirect("list.jsp");
%>