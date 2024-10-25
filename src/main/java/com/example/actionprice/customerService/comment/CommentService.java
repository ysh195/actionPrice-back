package com.example.actionprice.customerService.comment;

import java.util.List;

public interface CommentService {

    Comment createComment(Integer postId, String username, String content);
    Comment updateComment(Integer commentId, String logined_username, String content);
    void deleteComment(Integer commentId, String logined_username);

    List<Comment> getCommentListByPostId(Integer postId);
    List<Comment> getCommentListByUsername(String username);
}
