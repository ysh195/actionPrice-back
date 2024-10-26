package com.example.actionprice.customerService.post.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

// "/api/post/{id}/detail" 를 위한 PostListDTO에 사용됨
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostSimpleDTO {
  private Integer postId;
  private String title;
  private String content;
  private boolean published;
  private String username;
  private LocalDateTime createdAt;
  private int commentSize;

  public Integer getPostId() {
    return postId;
  }
}
