package com.example.actionprice.customerService.comment;

import com.example.actionprice.customerService.post.Post;
import com.example.actionprice.customerService.post.PostRepository;
import com.example.actionprice.exception.PostNotFoundException;
import com.example.actionprice.exception.UserNotFoundException;
import com.example.actionprice.user.User;
import com.example.actionprice.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public Comment createComment(Integer postId, String username, String content) {

        User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException("user(" + username + ") does not exist"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("post(" + postId + ") does not exist"));

        Comment comment = Comment.builder()
                .content(content)
                .user(user)
                .post(post)
                .build();

        comment = commentRepository.save(comment);

        user.addComment(comment);
        userRepository.save(user);

        post.addComment(comment);
        postRepository.save(post);

        return comment;
    }

    @Override
    public Comment updateComment(Integer commentId, String logined_username, String content) {
        User user = userRepository.findById(logined_username)
                .orElseThrow(() -> new UserNotFoundException("user(" + logined_username + ") does not exist"));

        // comment not found 예외도 추가해야 함
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new UserNotFoundException("user(" + logined_username + ") does not exist"));
        return null;
    }

    @Override
    public void deleteComment(Integer commentId, String logined_username) {

    }

    @Override
    public List<Comment> getCommentListByPostId(Integer postId) {
        return List.of();
    }

    @Override
    public List<Comment> getCommentListByUsername(String username) {
        return List.of();
    }
}
