package com.example.actionprice.customerService.post.dto;

import com.example.actionprice.customerService.comment.CommentSimpleDTO;
import java.util.List;
import lombok.*;

import java.time.LocalDateTime;

// "/api/post/list?pageNum=0&key=abc" 에 사용됨
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private boolean hasNext;  // 현재가 마지막 comment 페이지인지
}
