package com.example.actionprice.customerService.post;

import com.example.actionprice.customerService.post.dto.PostDetailDTO;

import com.example.actionprice.customerService.post.dto.PostListDTO;
import com.example.actionprice.customerService.post.dto.PostSimpleDTO;
import java.util.List;

public interface PostService {

    Integer createPost(PostForm form);
    PostSimpleDTO goUpdatePost(Integer postId, String logined_username);
    String updatePost(Integer postId,PostForm form);
    String deletePost(Integer postId, String logined_username);

    PostDetailDTO getDetailPost(Integer postId, int commentPageNum);

    PostListDTO getPostList(int pageNum, String keyword);
    PostListDTO getPostListForMyPage(String username, String keyword, int pageNum);

}
