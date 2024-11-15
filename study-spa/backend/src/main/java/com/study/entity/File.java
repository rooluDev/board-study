package com.study.entity;

import com.study.dto.FileDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * File Entity
 */
@Getter
@Builder
public class File {
    private final Long fileId;
    private final Long boardId;
    private final String originalName;
    private final String physicalName;
    private final String filePath;
    private final String extension;
    private final Long size;
    private final Timestamp createdAt;
    private final Timestamp editedAt;

    /**
     * entity -> fileDto
     * @return
     */
    public FileDto toFileDto(){
        return FileDto.builder()
                .fileId(fileId)
                .originalName(originalName)
                .physicalName(physicalName)
                .filePath(filePath)
                .extension(extension)
                .build();
    }
}
