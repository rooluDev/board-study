package com.study.service;

import com.study.dto.FileDto;
import com.study.exception.FileNotFoundException;
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
import java.util.stream.Collectors;

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
     * board와 관련된 file들 가져오기
     * @param boardId
     * @return
     */
    public List<FileDto> findFileListByBoardId(Long boardId){
        // Entity -> Dto
        List<FileDto> fileDtoList = fileMapper.findByBoardId(boardId).stream()
                .map(entity -> entity.toFileDto())
                .collect(Collectors.toList());

        return fileDtoList;
    }

    /**
     * File Upload
     *
     * @param multipartFiles
     * @param boardId
     */
    public void addFile(MultipartFile[] multipartFiles, Long boardId) throws IOException {
        if(multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                if (!multipartFile.isEmpty()) {
                    // File DTO 생성
                    com.study.entity.File file = com.study.entity.File.builder()
                            .boardId(boardId)
                            .originalName(multipartFile.getOriginalFilename())
                            .physicalName(UUID.randomUUID().toString())
                            .filePath(path)
                            .extension(MultipartFileUtils.extractExtension(multipartFile.getOriginalFilename()))
                            .size(multipartFile.getSize())
                            .build();

                    // Server 저장
                    saveFileRepository.createFile(file.toFileDto(),multipartFile);

                    // File DB Add
                    fileMapper.createFile(file);
                }
            }
        }
    }

    /**
     * 게시물의 등록된 파일 DB에서 삭제
     * @param boardId
     */
    public int deleteFilesByBoardId(Long boardId){
        return fileMapper.deleteByBoardId(boardId);
    }

    /**
     * 하나의 파일 삭제
     * @param fileId
     */
    public int deleteFileByFileId(Long fileId){
        return fileMapper.deleteById(fileId);
    }

    /**
     * pk로 파일 정보 가져오기
     * @param fileId
     * @return
     */
    public FileDto findByFileId(Long fileId) throws FileNotFoundException {
        return fileMapper.findByFileId(fileId).orElseThrow(() -> new com.study.exception.FileNotFoundException()).toFileDto();
    }
}
