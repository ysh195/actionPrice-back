package com.example.actionprice.customerService.comment;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * PostDetail에서 Comment 리스트를 표현할 때 각 Comment 정보를 담아줄 객체
 * @value commentId
 * @value postId : 연결된 post의 id
 * @value username : 연결된 user의 username
 * @value content
 * @value createdAt
 * @author 연상훈
 * @created 2024-10-27 오후 12:48
 * @info List<CommentSimpleDTO> 형태로 PostDetailDTO에 포함됨
 */
@Builder
@Getter
@ToString
public class CommentSimpleDTO {
  private Integer commentId;
  private Integer postId;
  private String username;
  private String content;
  private LocalDateTime createdAt;
}
