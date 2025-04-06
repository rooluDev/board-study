package com.study.command;

import com.study.DAO.BoardDAO;
import com.study.DAO.FileDAO;
import com.study.DTO.BoardDTO;
import com.study.DTO.FileDTO;
import com.study.properties.FileProperties;
import com.study.validate.BoardFormValidate;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 게시판 수정 프로세스
 */
public class EditProcCommand implements Command {

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
}
