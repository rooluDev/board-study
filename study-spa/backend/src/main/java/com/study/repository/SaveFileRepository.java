package com.study.repository;

import com.study.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 파일의 메타데이터 저장하는 인터페이스
 */
public interface SaveFileRepository{

    /**
     * 파일 저장
     *
     * @param fileDto 파일 정보
     * @param multipartFile multipartFile
     * @throws IOException IOException
     */
    void createFile(FileDto fileDto, MultipartFile multipartFile) throws IOException;
}
