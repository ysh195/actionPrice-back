package com.example.actionprice.customerService.post.dto;

import com.example.actionprice.customerService.comment.Comment;
import com.example.actionprice.customerService.post.Post;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

// "/api/post/{id}/detail" 에 사용됨
public class PostListDTO {

  private List<PostSimpleDTO> postList;
  private int currentPageNum; // 현재 페이지 번호
  private int currentPageSize; // 현재 페이지에 존재하는 post의 갯수
  private final int itemSizePerPage = 10; // 페이지당 post의 갯수
  private long listSize; // 총 post 갯수
  private int totalPageNum; // 총 페이지
  private boolean hasNext; // 현재가 마지막 페이지인지
  private String keyword; // 검색에 사용된 키워드

  public PostListDTO(Page<Post> postPages, String keyword) {
    this.postList = postPages.getContent()
        .stream()
        .map(post -> {
          Set<Comment> commentSet = post.getCommentSet();
          int commentSize = (commentSet == null || commentSet.isEmpty()) ? 0 : commentSet.size();
          return PostSimpleDTO.builder()
            .postId(post.getPostId())
            .title(post.getTitle())
            .published(post.isPublished())
            .username(post.getUser().getUsername())
            .createdAt(post.getCreatedAt())
            .commentSize(commentSize)
            .build();
        })
        .collect(Collectors.toList());

    this.currentPageNum = postPages.getNumber();
    this.currentPageSize = postPages.getNumberOfElements();
    this.listSize = postPages.getTotalElements();
    this.totalPageNum = postPages.getTotalPages();
    this.hasNext = postPages.hasNext();
    this.keyword = keyword;
  }
}
