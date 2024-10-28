package com.example.actionprice.customerService.post.dto;

import com.example.actionprice.customerService.comment.CommentSimpleDTO;
import java.util.List;
import lombok.*;

import java.time.LocalDateTime;

/**
 * PostDetail에서 해당 post의 내용과 그 포스트에 연결된 댓글들을 보여주는 기능을 위해 사용됨
 * @value postId
 * @value username
 * @value title
 * @value content
 * @value published
 * @value createdAt
 * @value commentList : comment 리스트. List<CommentSimpleDTO>
 * @value currentPageNum : 현재 comment 페이지 번호
 * @value currentPageSize : 현재 페이지에 존재하는 comment의 갯수
 * @value itemSizePerPage : 페이지당 comment의 갯수(10개로 고정됨)
 * @value listSize : 총 comment 갯수
 * @value totalPageNum : 총 comment 페이지
 * @value hasNext : 다음 페이지가 있는지(= 현재가 마지막 페이지가 아닌지)
 * @author 연상훈
 * @created 2024-10-27 오후 12:49
 * @info CommentService의 기능을 일부 이용해야 해서 생성자로 입력 받으면서 한 번에 처리하지 않고, Builder로 처리함.
 * 고작 해야 DTO인데 CommentService를 이용하기엔 너무 무거워짐
 * @see "/api/post/{id}/detail" 에 사용됨
 */
@Builder
@Getter
@ToString
public class PostDetailDTO {
    private Integer postId;
    private String username;
    private String title;
    private String content;
    private boolean published;
    private LocalDateTime createdAt;

    private List<CommentSimpleDTO> commentList; // comment 리스트
    private int currentPageNum; // 현재 comment 페이지 번호
    private int currentPageSize; // 현재 페이지에 존재하는 comment의 갯수
    private final int itemSizePerPage = 10; // 페이지당 comment의 갯수
    private long listSize; // 총 comment 갯수
    private int totalPageNum; // 총 comment 페이지
    private boolean hasNext;  // 다음 페이지가 있는지(= 현재가 마지막 페이지가 아닌지)
}
