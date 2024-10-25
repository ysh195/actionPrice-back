package com.example.actionprice.customerService.post;

import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    Integer createPost(PostForm form);
    PostDetailDTO updatePost(Integer postId,PostForm form);
    void deletePost(Integer postId);

    PostDetailDTO getDetailPost(Integer postId);

    List<PostDetailDTO> getPostList(int page, String keyword);

}
