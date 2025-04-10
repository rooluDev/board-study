package com.study.service;

import com.study.dto.FileDTO;
import com.study.mapper.FileMapper;
import com.study.utils.MultipartFileUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * File Service
 */
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileMapper fileMapper;
    static final String REAL_PATH = "/Users/user/upload/";

    /**
     * File Upload
     *
     * @param fileList 저장할 파일 리스트
     * @param boardId  board PK
     */
    public void uploadFile(List<MultipartFile> fileList, Long boardId) throws IOException {
        for (MultipartFile multipartFile : fileList) {
            if (!multipartFile.isEmpty()) {
                // File DTO 생성
                FileDTO file = FileDTO.builder()
                        .boardId(boardId)
                        .originalName(multipartFile.getOriginalFilename())
                        .physicalName(UUID.randomUUID().toString())
                        .filePath(REAL_PATH)
                        .extension(MultipartFileUtils.extractExtension(multipartFile.getOriginalFilename()))
                        .size(multipartFile.getSize())
                        .build();

                // Server 저장
                String filePath = REAL_PATH + file.getPhysicalName() + "." + file.getExtension();
                File uploadedFile = new File(filePath);
                FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), uploadedFile);

                // File DB Add
                fileMapper.insertFile(file);
            }
        }
    }

    /**
     * 게시물에 저장된 파일들 가져오기
     *
     * @param boardId board Pk
     * @return 게시물에 있는 천부파일 리스트
     */
    public List<FileDTO> getFileListByBoardId(Long boardId) {
        return fileMapper.selectByBoardId(boardId);
    }

    /**
     * 단일 파일 찾기
     *
     * @param fileId pk
     * @return 파일
     */
    public FileDTO getFile(Long fileId) {
        return fileMapper.selectById(fileId);
    }

    /**
     * 게시물에 저장된 파일들 삭제
     *
     * @param boardId board Pk
     */
    public void deleteFileListByBoardId(Long boardId) {
        fileMapper.deleteByBoardId(boardId);
    }

    /**
     * 단일 파일 삭제
     *
     * @param fileId Pk
     */
    public void deleteById(Long fileId) {
        fileMapper.deleteById(fileId);
    }
}
