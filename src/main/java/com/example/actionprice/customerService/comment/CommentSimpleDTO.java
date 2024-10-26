package com.example.actionprice.customerService.comment;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentSimpleDTO {
  private Integer commentId;
  private Integer postId;
  private String username;
  private String content;
  private LocalDateTime createdAt;
}
