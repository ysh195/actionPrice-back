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

    /**
     * 댓글 생성
     * @param postId 어떤 게시글에 댓글을 추가해야 할 지 파악
     * @param logined_username 어떤 사용자에 댓글을 추가해야 할 지 파악
     * @param content 댓글 내용
     * @author 연상훈
     * @created 2024-10-27 오후 12:32
     * @throws UserNotFoundException 해당 username을 가진 사용자가 존재하지 않음
     * @throws PostNotFoundException 해당 id를 가진 post가 존재하지 않음
     */
    @Override
    public void createComment(Integer postId, String logined_username, String content) {

        User user = userRepository.findById(logined_username)
                .orElseThrow(() -> new UserNotFoundException("user(" + logined_username + ") does not exist"));

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
    }

    /**
     * 댓글 생성
     * @param commentId : 어떤 댓글의 내용을 수정해야 할 지 파악
     * @param logined_username : 댓글을 수정하려는 사용자(현재 로그인 한 사용자)와 댓글을 작성한 사용자가 일치하는 지 확인하는 용도
     * @param content : 수정할 댓글 내용
     * @author 연상훈
     * @created 2024-10-27 오후 12:32
     * @throws CommentNotFoundException 해당 id를 가진 comment가 존재하지 않음
     */
    @Override
    public void updateComment(Integer commentId, String logined_username, String content) {

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException("comment(id : " + commentId + ") does not exist"));

        if(!logined_username.equals(comment.getUser().getUsername())) {
            log.error("댓글을 작성한 사용자와 수정을 시도하려는 사용자의 username이 일치하지 않습니다.");
            return;
        }

        comment.setContent(content);
        commentRepository.save(comment);
    }

    /**
     * 댓글 생성
     * @param commentId : 어떤 댓글을 삭제해야 할 지 파악
     * @param logined_username : 댓글을 삭제하려는 사용자(현재 로그인 한 사용자)와 댓글을 작성한 사용자가 일치하는 지 확인하는 용도
     * @author 연상훈
     * @created 2024-10-27 오후 12:32
     * @throws CommentNotFoundException 해당 id를 가진 comment가 존재하지 않음
     */
    @Override
    public boolean deleteComment(Integer commentId, String logined_username) {

        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException("comment(id : " + commentId + ") does not exist"));

        if(!logined_username.equals(comment.getUser().getUsername())) {
            log.error("댓글을 작성한 사용자와 삭제를 시도하려는 사용자의 username이 일치하지 않습니다.");
            return false;
        }

        commentRepository.delete(comment);

        return true;
    }

    /**
     * PostDetail에 들어갈 댓글 목록 조회
     * @param postId 어떤 post의 댓글 목록을 조회하는 지 파악하는 용도
     * @param pageNum 해당 post에 포함된 다수의 댓글 페이지 중 어느 페이지인지 파악하는 용도
     * @author 연상훈
     * @created 2024-10-27 오후 12:40
     * @info Page<Comment> 형태로 값을 반환함. List로 변환하는 등의 작업은 PostSerivce에서 처리
     */
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

    /**
     * MyPage에 들어갈 댓글 목록 조회
     * @param username 어떤 사용자의 댓글 목록을 조회하는 지 파악하는 용도
     * @param pageNum 해당 post에 포함된 다수의 댓글 페이지 중 어느 페이지인지 파악하는 용도
     * @author 연상훈
     * @created 2024-10-27 오후 12:40
     * @info Page<Comment> 형태로 값을 반환함. MyPage에서 자신이 작성한 댓글을 열람하는 기능에 사용됨.
     * 근데 자기 댓글 조회 기능은 지금 구현되어 있지 않음
     */
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

    /**
     * Page<Comment> 형태의 값을 List<CommentSimpleDTO>로 변환하는 기능
     * @param commentPage Page<Comment> 타입의 값
     * @author 연상훈
     * @created 2024-10-27 오후 12:45
     * @info PostService에서 PostDetail을 처리할 때 사용할 기능이지만, 이게 은근히 길어서 메서드로 간단하게 처리.
     * PostService에 들어가기엔 관심사가 맞지 않는 기능이라 판단되어 CommentService에서 구현하고, 그걸 PostService에서 가져다 사용함
     * @info 변환할 Page<Comment>가 존재하지 않는 경우에 대해서는 이걸 사용하는 곳에서 알아서 처리하니까 굳이 여기서 다시 처리하지 않아도 됨
     */
    @Override
    public List<CommentSimpleDTO> convertCommentPageToCommentSimpleDTOList(Page<Comment> commentPage) {
        return commentPage.getContent()
            .stream()
            .map(comment -> CommentSimpleDTO.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getPost().getPostId())
                .username(comment.getUser().getUsername())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build())
            .collect(Collectors.toList());
    }
}
