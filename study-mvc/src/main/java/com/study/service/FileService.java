package com.study.service;

import com.study.dto.FileDTO;
import com.study.mapper.FileMapper;
import com.study.utils.MultipartFileUtils;
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
public class FileService {
    private final FileMapper fileMapper;
    static final String REAL_PATH = "/Users/user/upload/";

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    /**
     * File Upload
     *
     * @param multipartFiles
     * @param boardId
     */
    public void uploadFile(MultipartFile[] multipartFiles, Long boardId) throws IOException {
        if(multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
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
                    fileMapper.createFile(file);
                }
            }
        }
    }

    /**
     * 게시물에 저장된 파일들 가져오기
     * @param boardId
     * @return
     */
    public List<FileDTO> findFiles(Long boardId){
        return fileMapper.findByBoardId(boardId);
    }

    /**
     * 파일 하나 찾기
     * @param fileId
     * @return
     */
    public FileDTO findOne(Long fileId){
        return fileMapper.findByFileId(fileId);
    }

    /**
     * 게시물에 저장된 파일들 삭제
     * @param boardId
     */
    public void deleteFiles(Long boardId){
        fileMapper.deleteByBoardId(boardId);
    }

    /**
     * pk들로 파일 지우기
     * @param fileIdList
     */
    public void deleteSelectedFiles(List<Long> fileIdList){
        for(Long fileId : fileIdList){
            fileMapper.deleteById(fileId);
        }
    }
}
