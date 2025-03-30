package com.study.service;

import com.study.dto.File;
import com.study.repository.FileRepository;

import java.util.List;

/**
 * File Service 계층
 */
public class FileService {

    private final FileRepository fileRepository = FileRepository.getInstance();

    /**
     * 첨부파일 저장
     *
     * @param fileList 저장할 파일들
     */
    public void addFileList(List<File> fileList) {
        fileList.forEach(file -> {
            try {
                fileRepository.insertFile(file);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 게시물에 있는 첨부파일 데이터 가져오기
     *
     * @param boardId 게시물 PK
     * @return 게시물에 있는 첨부파일 리스트
     */
    public List<File> getFileListByBoardId(int boardId) throws Exception {
        return fileRepository.selectFileListByBoardId(boardId);
    }

    /**
     * pk로 파일 삭제
     *
     * @param fileId pk
     * @throws Exception
     */
    public void deleteById(int fileId) throws Exception {
        fileRepository.deleteById(fileId);
    }

    /**
     * 게시물에 있는 모든 파일 삭제
     *
     * @param boardId board PK
     * @throws Exception
     */
    public void deleteByBoardId(int boardId) throws Exception {
        fileRepository.deleteByBoardId(boardId);
    }

    /**
     * 특정 파일 데이터 가져오기
     *
     * @param fileId pk
     * @return 특정 파일
     * @throws Exception
     */
    public File getFileById(int fileId) throws Exception {
        return fileRepository.selectFileById(fileId);
    }

}
