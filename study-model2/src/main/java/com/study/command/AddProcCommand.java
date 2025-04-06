package com.study.command;

import com.study.DAO.BoardDAO;
import com.study.DAO.FileDAO;
import com.study.DTO.BoardDTO;
import com.study.DTO.FileDTO;
import com.study.properties.FileProperties;
import com.study.validate.BoardFormValidate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

/**
 * 게시판 추가 로직 프로세스
 */
public class AddProcCommand implements Command {

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

}