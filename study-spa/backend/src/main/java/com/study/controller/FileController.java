package com.study.controller;

import com.study.dto.FileDto;
import com.study.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * file rest api controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FileController {

    private final FileService fileService;

    /**
     * 첨부파일 리소스
     *
     * @param fileId pk
     * @return responseEntity
     */
    @GetMapping("/file/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable(name = "fileId") Long fileId) throws IOException {
        // 파일 정보 가져오기
        FileDto file = fileService.getFile(fileId);

        // 파일 정보 설정
        String filePathString = file.getFilePath() + file.getPhysicalName() + "." + file.getExtension();
        File filePath = Paths.get(filePathString).toFile();
        Resource resource = null;

        resource = new InputStreamResource(Files.newInputStream(filePath.toPath()));


        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getName() + "\"")
                .body(resource);
    }
}
