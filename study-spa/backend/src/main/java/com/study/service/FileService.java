package com.study.service;

import com.study.dto.FileDto;
import com.study.mapper.FileMapper;
import com.study.repository.LocalSaveFileRepository;
import com.study.repository.SaveFileRepository;
import com.study.utils.MultipartFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * 파일 관련 비지니스 로직
 */
@Service
public class FileService {
    @Value("#{file['file.path']}")
    private String path;
    private final FileMapper fileMapper;
    private final SaveFileRepository saveFileRepository;

    @Autowired
    public FileService(FileMapper fileMapper, LocalSaveFileRepository localSaveFileRepository) {
        this.fileMapper = fileMapper;
        this.saveFileRepository = localSaveFileRepository;
    }


    /**
     * 게시물에 저장된 파일들 가져오기
     *
     * @param boardId board Pk
     * @return 게시물에 있는 천부파일 리스트
     */
    public List<FileDto> getFileListByBoardId(Long boardId) {
        return fileMapper.selectByBoardId(boardId);
    }
    /**
     * 단일 파일 찾기
     *
     * @param fileId pk
     * @return 파일
     */
    public FileDto getFile(Long fileId) {
        return fileMapper.selectById(fileId);
    }

    /**
     * File Upload
     *
     * @param multipartFiles
     * @param boardId
     */
    public void addFile(List<MultipartFile> multipartFiles, Long boardId) throws IOException {
        if(multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                if (!multipartFile.isEmpty()) {
                    // File DTO 생성
                    FileDto file = FileDto.builder()
                            .boardId(boardId)
                            .originalName(multipartFile.getOriginalFilename())
                            .physicalName(UUID.randomUUID().toString())
                            .filePath(path)
                            .extension(MultipartFileUtils.extractExtension(multipartFile.getOriginalFilename()))
                            .size(multipartFile.getSize())
                            .build();

                    // Server 저장
                    saveFileRepository.createFile(file,multipartFile);

                    // File DB Add
                    fileMapper.insertFile(file);
                }
            }
        }
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
