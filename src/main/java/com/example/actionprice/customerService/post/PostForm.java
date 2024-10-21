package com.example.actionprice.customerService.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostForm {

    @NotBlank(groups = PostUpdateGroup.class)
    private Integer postId;

    @NotBlank(groups = {PostCreateGroup.class, PostUpdateGroup.class})
    private String title;

    @NotBlank(groups = {PostCreateGroup.class, PostUpdateGroup.class})
    private String content;

    @NotBlank(groups = {PostCreateGroup.class})
    private boolean published;

    public interface PostCreateGroup{}
    public interface PostUpdateGroup {}
}
