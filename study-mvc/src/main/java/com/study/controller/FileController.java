package com.study.controller;

import com.study.dto.FileDTO;
import com.study.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.*;

/**
 * File 관련 컨트롤러
 */
@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 파일 다운로드
     *
     * @param fileId   pathVariable
     * @param response HttpServletResponse
     * @throws IOException IOException
     */
    @GetMapping(value = {"/file/download/{fileId}"})
    public void downloadFile(@PathVariable Long fileId, HttpServletResponse response) throws IOException {
        // 파일 정보 가져오기
        FileDTO file = fileService.getFile(fileId);

        // 파일 정보 설정
        String fileName = file.getPhysicalName() + "." + file.getExtension();
        String filePath = file.getFilePath() + "/" + fileName;
        File fileDownload = new File(filePath);

        // response 설정
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        // 파일 다운로드
        download(fileDownload, response.getOutputStream());
    }

    private void download(File file, OutputStream outputStream) throws IOException {
        // try-with-resource 파일 다운로드
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);
             BufferedOutputStream bos = new BufferedOutputStream(outputStream)) {

            byte[] buffer = new byte[1024 * 1024 * 5];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        }
    }


}
