package com.study.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 게시판 - 목록에 필요한 정보들
 */
@Getter
@Setter
@Builder
public class BoardListDtoForListPage {
    private int totalCount;
    private BoardSearchFormDto searchCondition;
    private List<BoardDto> boardList;
}
