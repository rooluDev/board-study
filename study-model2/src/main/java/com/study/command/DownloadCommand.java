package com.study.command;

import com.study.DAO.FileDAO;
import com.study.DTO.FileDTO;

import java.io.*;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 첨부파일 다운로드
 */
public class DownloadCommand implements Command {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int fileId = Integer.parseInt(request.getParameter("fileId"));

        FileDAO fileDAO = new FileDAO();
        FileDTO file = fileDAO.selectFileById(fileId);

        String path = file.getFilePath();
        String fileName = file.getPhysicalName();
        String extension = file.getExtension();
        String fileFullPath = path + "/" + fileName + "." + extension;
        String encodedFileName = URLEncoder.encode(file.getOriginalName(), "UTF-8").replaceAll("\\+", "%20");
        File fileDownload = new File(fileFullPath);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + encodedFileName + "\"");

        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileDownload));
             BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream())) {
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        }
    }
}
