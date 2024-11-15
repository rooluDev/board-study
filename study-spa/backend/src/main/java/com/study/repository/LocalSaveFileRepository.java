package com.study.repository;

import com.study.dto.FileDto;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * File을 Server에 저장하는 레포 클래스
 */
@Repository
public class LocalSaveFileRepository implements SaveFileRepository{
    @Value("#{file['file.path']}")
    private String path;

    /**
     * server에 파일 저장
     * @param fileDto
     * @param multipartFile
     * @throws IOException
     */
    public void createFile(FileDto fileDto , MultipartFile multipartFile) throws IOException {
        String filePath = path + fileDto.getPhysicalName() + "." + fileDto.getExtension();
        File uploadedFile = new File(filePath);
        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), uploadedFile);
    }
}
