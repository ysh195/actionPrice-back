package com.example.actionprice.customerService.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Log4j2
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/detail")
    public String createComment(
            @PathVariable("postId") int postId,
            String username,
            String content
    ) {
        return "";
    }

    @PostMapping("/{postId}/detail/{commentId}/update")
    public String updateComment(
            @PathVariable("postId") int postId,
            @PathVariable("commentId") int commentId,
            String logined_username,
            String content
    ) {

        return "";
    }

    @PostMapping("/{postId}/detail/{commentId}/delete")
    public String deleteComment(
            @PathVariable("postId") int postId,
            @PathVariable("commentId") int commentId,
            String logined_username
    ) {
        return "";
    }
}
