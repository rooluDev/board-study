package com.study.mapper;

import com.study.dto.FileDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * FileDTO mapper
 */
@Mapper
public interface FileMapper {
    /**
     * File Create
     * @param file
     */
    void createFile(FileDTO file);

    /**
     * boardId로 files 찾기
     * @param boardId
     * @return
     */
    List<FileDTO> findByBoardId(Long boardId);

    /**
     * pk로 파일 찾기
     * @param fileId
     * @return
     */
    FileDTO findByFileId(Long fileId);

    /**
     * boardId 일치하는 파일들 삭제
     * @param boardId
     */
    void deleteByBoardId(Long boardId);

    /**
     * pk로 파일 삭제하기
     * @param fileId
     */
    void deleteById(Long fileId);
}
