package com.example.actionprice.customerService.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class CommentListDTO {
    private List<CommentSimpleDTO> commentList; // comment 리스트
    private int currentPageNum; // 현재 comment 페이지 번호
    private int currentPageSize; // 현재 페이지에 존재하는 comment의 갯수
    private final int itemSizePerPage = 10; // 페이지당 comment의 갯수
    private long listSize; // 총 comment 갯수
    private int totalPageNum; // 총 comment 페이지
    private boolean hasNext;  // 다음 페이지가 있는지(= 현재가 마지막 페이지가 아닌지)
}
