package com.study.command;

import com.study.DAO.FileDAO;
import com.study.DTO.FileDTO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadCommand implements Command {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int fileId = Integer.parseInt(request.getParameter("fileId"));
        int boardId = Integer.parseInt(request.getParameter("boardId"));
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        FileDAO fileDAO = new FileDAO();
        FileDTO file = fileDAO.findById(fileId);
        String path = file.getFilePath();
        String fileName = file.getPhysicalName() + "." + file.getExtension();
        String filePath = path + "/" + fileName;
        File fileDownload = new File(filePath);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        OutputStream out = response.getOutputStream();
        FileInputStream fis = null;
        byte[] buffer = new byte[5242880];

        try {
            fis = new FileInputStream(fileDownload);

            int temp;
            while((temp = fis.read(buffer)) != -1) {
                out.write(buffer, 0, temp);
            }
        } catch (IOException var19) {
            var19.printStackTrace();
        } finally {
            if (fis != null) {
                fis.close();
            }

            if (out != null) {
                out.close();
            }

        }

        response.sendRedirect("/board?command=list&boardId=" + boardId + "&pageNum=" + pageNum);
    }
}
