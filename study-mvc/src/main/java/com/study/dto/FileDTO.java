package com.study.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * FileDTO
 */
@Getter
@Setter
@Builder
public class FileDTO {
    private Long fileId;
    private Long boardId;
    private String originalName;
    private String physicalName;
    private String filePath;
    private String extension;
    private Long size;
    private Timestamp createdAt;
    private Timestamp editedAt;
}
