package com.example.actionprice.customerService.post;

public interface PostService {

    String createPost(String username,PostForm form);
    String updatePost(Integer postId,PostForm form);
    String deletePost(Integer postId);
}
