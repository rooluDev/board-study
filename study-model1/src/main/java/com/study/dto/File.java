package com.study.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * 파일 Dto
 */
@Getter
@Setter
@Builder
public class File {
    private int fileId;
    private int boardId;
    private String originalName;
    private String physicalName;
    private String filePath;
    private String extension;
    private int size;
    private Timestamp createdAd;
    private Timestamp editedAd;
}
