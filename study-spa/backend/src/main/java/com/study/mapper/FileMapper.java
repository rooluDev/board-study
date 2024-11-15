package com.study.mapper;

import com.study.dto.FileDto;
import com.study.entity.File;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FileMapper {
    /**
     * boardId로 files 찾기
     * @param boardId
     * @return
     */
    List<File> findByBoardId(Long boardId);

    /**
     * File Create
     * @param file
     */
    int createFile(File file);

    /**
     * board의 파일 삭제
     * @param boardId
     */
    int deleteByBoardId(Long boardId);

    /**
     * pk로 파일 찾기
     * @param fileId
     * @return
     */
    Optional<File> findByFileId(Long fileId);

    /**
     * pk로 파일 삭제
     * @param fileId
     */
    int deleteById(Long fileId);
}
