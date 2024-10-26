package com.example.actionprice.customerService.comment;

import com.example.actionprice.customerService.post.Post;
import com.example.actionprice.customerService.post.PostRepository;
import com.example.actionprice.exception.CommentNotFoundException;
import com.example.actionprice.exception.PostNotFoundException;
import com.example.actionprice.exception.UserNotFoundException;
import com.example.actionprice.user.User;
import com.example.actionprice.user.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public CommentSimpleDTO createComment(Integer postId, String username, String content) {

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

        return convertCommentToSimpleDTO(comment);
    }

    @Override
    public CommentSimpleDTO updateComment(Integer commentId, String logined_username, String content) {
        User user = userRepository.findById(logined_username)
                .orElseThrow(() -> new UserNotFoundException("user(" + logined_username + ") does not exist"));

        // comment not found 예외도 추가해야 함
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("comment(id : " + commentId + ") does not exist"));

        if(!logined_username.equals(comment.getUser().getUsername())) {
            return null;
        }

        comment.setContent(content);
        comment = commentRepository.save(comment);

        return convertCommentToSimpleDTO(comment);
    }

    @Override
    public boolean deleteComment(Integer commentId, String logined_username) {
        User user = userRepository.findById(logined_username)
            .orElseThrow(() -> new UserNotFoundException("user(" + logined_username + ") does not exist"));

        // comment not found 예외도 추가해야 함
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("comment(id : " + commentId + ") does not exist"));

        if(!logined_username.equals(comment.getUser().getUsername())) {
            return false;
        }

        commentRepository.delete(comment);

        return true;
    }

    @Override
    public Page<Comment> getCommentListByPostId(Integer postId, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, 10, Sort.by(Sort.Order.desc("commentId")));
        Page<Comment> commentPage = commentRepository.findByPost_PostId(postId, pageable);
        if(!commentPage.hasContent()) {
            // 없을 수도 있으니 에러로 처리하면 안 됨
            return null;
        }
        return commentPage;
    }

    @Override
    public Page<Comment> getCommentListByUsername(String username, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, 10, Sort.by(Sort.Order.desc("commentId")));
        Page<Comment> commentPage = commentRepository.findByUser_Username(username, pageable);
        if(!commentPage.hasContent()) {
            // 없을 수도 있으니 에러로 처리하면 안 됨
            return null;
        }
        return commentPage;
    }

    @Override
    public List<CommentSimpleDTO> convertCommentPageToCommentSimpleDTOList(Page<Comment> commentPage) {
        return commentPage.getContent()
            .stream()
            .map(comment -> convertCommentToSimpleDTO(comment))
            .collect(Collectors.toList());
    }

    private CommentSimpleDTO convertCommentToSimpleDTO(Comment comment) {
        return CommentSimpleDTO.builder()
            .commentId(comment.getCommentId())
            .postId(comment.getPost().getPostId())
            .username(comment.getUser().getUsername())
            .content(comment.getContent())
            .createdAt(comment.getCreatedAt())
            .build();
    }
}
