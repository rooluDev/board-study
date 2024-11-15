package com.study.DTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class FileDTO {
    private int fileId;
    private int boardId;
    private String originalName;
    private String physicalName;
    private String filePath;
    private String extension;
    private int size;
    private Timestamp createdAt;
    private Timestamp editedAt;

}
