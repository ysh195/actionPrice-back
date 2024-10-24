package com.example.actionprice.customerService.post;

import com.example.actionprice.customerService.comment.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailDTO {
    private Integer postId;
    private String username;
    private String title;
    private String content;
    private boolean published;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<Comment> commentSet;
}
