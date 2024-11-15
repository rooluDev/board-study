package com.study.command;

import com.study.DAO.BoardDAO;
import com.study.DAO.FileDAO;
import com.study.DTO.BoardDTO;
import com.study.DTO.FileDTO;
import com.study.exception.CategoryException;
import com.study.exception.ContentException;
import com.study.exception.TitleException;
import com.study.exception.UserNameException;
import com.study.properties.FileProperties;
import com.study.utils.StringUtils;
import com.study.validate.BoardFormValidate;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

public class AddProcCommand implements Command {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FileProperties fileProperties = new FileProperties();
        Map<String, String> propertiesMap = fileProperties.getProperties();
        String path = (String)propertiesMap.get("filePath");
        String characterSet = (String)propertiesMap.get("characterSet");
        request.setCharacterEncoding(characterSet);
        response.setContentType("text/html; charset=UTF-8");
        BoardDTO board = new BoardDTO();
        List<FileDTO> fileList = new ArrayList();
        BoardFormValidate boardFormValidate = new BoardFormValidate();
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        diskFileItemFactory.setRepository(new File(path));
        diskFileItemFactory.setDefaultCharset(characterSet);
        ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);

        try {
            List<FileItem> fileItemList = fileUpload.parseRequest(request);

            for(FileItem item : fileItemList) {
                String fieldName;
                if (!item.isFormField()) {
                    fieldName = item.getName();
                    if (!StringUtils.isNull(fieldName)) {
                        Long size = item.getSize();
                        String physicalName = UUID.randomUUID().toString();
                        String extension = FilenameUtils.getExtension(fieldName);
                        String separator = File.separator;
                        FileDTO file = new FileDTO();
                        file.setOriginalName(fieldName);
                        file.setPhysicalName(physicalName);
                        file.setFilePath((String)propertiesMap.get("filePath"));
                        file.setExtension(extension);
                        file.setSize(size.intValue());
                        String filePath = path + separator + physicalName + "." + extension;
                        File uploadedFile = new File(filePath);
                        item.write(uploadedFile);
                        fileList.add(file);
                    }
                } else {
                    fieldName = item.getFieldName();
                    String fieldValue = item.getString(characterSet);
                    if (fieldName.equals("category")) {
                        if (!boardFormValidate.validateCategory(Integer.parseInt(fieldValue))) {
                            throw new CategoryException("category value error");
                        }

                        board.setCategoryId(Integer.parseInt(fieldValue));
                    } else if (fieldName.equals("userName")) {
                        if (!boardFormValidate.validateUserName(fieldName)) {
                            throw new UserNameException("userName value error");
                        }

                        board.setUserName(fieldValue);
                    } else if (fieldName.equals("password")) {
                        if (!boardFormValidate.validatePassword(fieldName)) {
                        }

                        board.setPassword(fieldValue);
                    } else if (fieldName.equals("title")) {
                        if (!boardFormValidate.validateTitle(fieldName)) {
                            throw new TitleException("title value error");
                        }

                        board.setTitle(fieldValue);
                    } else if (fieldName.equals("content")) {
                        if (!boardFormValidate.validateContent(fieldName)) {
                            throw new ContentException("content value error");
                        }

                        board.setContent(fieldValue);
                    }
                }
            }
        } catch (FileUploadException var25) {
            var25.printStackTrace();
        } catch (IllegalStateException var26) {
            response.sendRedirect("/board?command=error");
        } catch (Exception var27) {
            throw new RuntimeException(var27);
        }

        BoardDAO boardDAO = new BoardDAO();
        int boardId = boardDAO.insertBoard(board);
        FileDAO fileDAO = new FileDAO();
        Iterator var31 = fileList.iterator();

        while(var31.hasNext()) {
            FileDTO uploadFile = (FileDTO)var31.next();
            System.out.println(uploadFile.getOriginalName());
            System.out.println(uploadFile.getExtension());
            uploadFile.setBoardId(boardId);
            fileDAO.insertFile(uploadFile);
        }

        response.sendRedirect("/board?command=list");
    }
}

