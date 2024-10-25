package com.example.actionprice.customerService.post;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostForm {

    @Min(value = 0, groups ={PostUpdateGroup.class})
    private Integer postId;

    @NotNull(groups ={PostCreateGroup.class, PostUpdateGroup.class})
    private String username;

    @NotNull(groups = {PostCreateGroup.class, PostUpdateGroup.class})
    private String title;

    @NotNull(groups = {PostCreateGroup.class, PostUpdateGroup.class})
    private String content;

//    @NotBlank(groups = {PostCreateGroup.class})
    private boolean published;

    public interface PostCreateGroup{}
    public interface PostUpdateGroup {}
}
