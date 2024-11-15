package com.study.repository;

import com.study.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SaveFileRepository{
    void createFile(FileDto fileDto, MultipartFile multipartFile) throws IOException;
}
