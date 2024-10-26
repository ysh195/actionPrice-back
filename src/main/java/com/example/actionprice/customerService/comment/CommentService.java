package com.example.actionprice.customerService.comment;

import java.util.List;
import org.springframework.data.domain.Page;

public interface CommentService {

    CommentSimpleDTO createComment(Integer postId, String username, String content);
    CommentSimpleDTO updateComment(Integer commentId, String logined_username, String content);
    boolean deleteComment(Integer commentId, String logined_username);

    Page<Comment> getCommentListByPostId(Integer postId, int pageNum);
    Page<Comment> getCommentListByUsername(String username, int pageNum);

    List<CommentSimpleDTO> convertCommentPageToCommentSimpleDTOList(Page<Comment> commentPage);

}
