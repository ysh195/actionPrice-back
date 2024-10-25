package com.example.actionprice.customerService.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    PostDetailDTO createPost(PostForm form);
    PostDetailDTO updatePost(Integer postId,PostForm form);
    void deletePost(Integer postId);

    PostDetailDTO getDetailPost(Integer postId);

    List<PostDetailDTO> getPostList(int page, String keyword);
    List<PostDetailDTO> getPostListForMyPage(String username, String keyword, int pageNumber);

}
