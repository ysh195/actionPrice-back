package com.example.actionprice.customerService.post;

import com.example.actionprice.customerService.post.dto.PostListDTO;
import com.example.actionprice.customerService.post.dto.PostSimpleDTO;

public interface PostService {

    PostSimpleDTO createPost(PostForm form);
    PostSimpleDTO goUpdatePost(Integer postId, String logined_username);
    PostSimpleDTO updatePost(Integer postId,PostForm form);
    PostSimpleDTO deletePost(Integer postId, String logined_username);
    PostSimpleDTO getDetailPost(Integer postId, Integer commentPageNum);

    PostListDTO getPostList(int pageNum, String keyword);
    PostListDTO getPostListForMyPage(String username, String keyword, Integer pageNum);

}
