package com.example.actionprice.customerService.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    String createPost(PostForm form);
    String updatePost(Integer postId,PostForm form);
    String deletePost(Integer postId);

    PostDetailDTO getDetailPost(Integer postId);

    Page<Post> getPostList(int page, String keyword);

}
