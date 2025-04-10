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
     *
     * @param file 저장할 파일
     */
    void insertFile(FileDTO file);

    /**
     * boardId로 fileList 찾기
     *
     * @param boardId board PK
     * @return 게시물에 있는 첨부파일 리스트
     */
    List<FileDTO> selectByBoardId(Long boardId);

    /**
     * pk로 파일 찾기
     *
     * @param fileId pk
     * @return 단일 파일
     */
    FileDTO selectById(Long fileId);

    /**
     * boardId 일치하는 파일들 삭제
     *
     * @param boardId board PK
     */
    void deleteByBoardId(Long boardId);

    /**
     * pk로 파일 삭제하기
     *
     * @param fileId PK
     */
    void deleteById(Long fileId);
}
