package com.study.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * FileDTO
 */
@Getter
@Setter
@Builder
public class FileDto {
    private Long fileId;
    private Long boardId;
    private String originalName;
    private String physicalName; // downloadFile() 내부에서 사용할 멤버변수
    private String filePath; // downloadFile() 내부에서 사용할 멤버변수
    private String extension; // downloadFile() 내부에서 사용할 멤버변수
    private Long size;
}
