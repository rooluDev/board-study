package com.study.returnType;

import com.study.dto.BoardDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * BoardList와 전체 row count 반환타입 클래스
 */
@Getter
@Setter
@Builder
public class BoardListAndCount {
    private List<BoardDTO> boardList;
    private int rowCount;
}
