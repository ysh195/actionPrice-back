package com.example.actionprice.customerService.post;

import com.example.actionprice.customerService.comment.Comment;
import com.example.actionprice.customerService.comment.CommentService;
import com.example.actionprice.customerService.post.dto.PostDetailDTO;
import com.example.actionprice.customerService.post.dto.PostListDTO;
import com.example.actionprice.customerService.post.dto.PostSimpleDTO;
import com.example.actionprice.exception.PostNotFoundException;
import com.example.actionprice.exception.UserNotFoundException;
import com.example.actionprice.user.User;
import com.example.actionprice.user.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

// comment 페이지 번호도 같이 줘야 함
@Service
@RequiredArgsConstructor
@Transactional //실패시 다같이 실패가 뜸
@Log4j2
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentService commentService;

    @Override
    public PostSimpleDTO createPost(PostForm form) {
        User user = userRepository.findById(form.getUsername())
            .orElseThrow(() -> new UserNotFoundException("user(" + form.getUsername() + ") does not exist"));

        Post post = Post.builder()
                .user(user)
                .title(form.getTitle())
                .content(form.getContent())
                .published(form.isPublished())
                .build();
        post = postRepository.save(post);

        user.addPost(post);
        userRepository.save(user);

        return convertPostToPostSimpleDTO(post, false);
    }

    @Override
    public PostSimpleDTO goUpdatePost(Integer postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("post(" + postId + ") does not exist"));

        return convertPostToPostSimpleDTO(post, true);
    }

    @Override
    public PostDetailDTO updatePost(Integer postId, PostForm form) {

        String username = form.getUsername();
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException("user(" + form.getUsername() + ") does not exist"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("post(" + postId + ") does not exist"));

        if(!username.equals(post.getUser().getUsername())) {
            return null;
        }

        post.setTitle(form.getTitle());
        post.setContent(form.getContent());

        post = postRepository.save(post);

        return convertPostToPostDetailDTO(post, 0);
    }

    @Override
    public void deletePost(Integer postId, String logined_username) {

        User user = userRepository.findById(logined_username)
                .orElseThrow(() -> new UserNotFoundException("user(" + logined_username + ") does not exist"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("post(" + postId + ") does not exist"));

        if(!logined_username.equals(post.getUser().getUsername())) {
            log.info("you are not the writer");
            return;
        }

        postRepository.delete(post);
    }

    @Override
    public PostListDTO getPostList(int page, String keyword) {
        log.info("[class] PostServiceImpl - [method] getPostList -  - page : {} | keyword : {}", page, keyword);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("postId")));
        Page<Post> postPage = null;

        if (keyword == null || keyword.isEmpty()) {
            // 키워드가 없을 경우 전체 목록 반환
            keyword = "";
            postPage = postRepository.findAll(pageable);
        } else {
            // 키워드가 있을 경우 제목에서 키워드를 검색
            postPage = postRepository.findByTitleContainingOrUser_UsernameContaining(keyword, keyword, pageable);
        }

        return new PostListDTO(postPage, keyword);
    }

    @Override
    public PostDetailDTO getDetailPost(Integer postId, int commentPageNum) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("post(" + postId + ") does not exist"));

        return convertPostToPostDetailDTO(post, commentPageNum);
    }

    @Override
    public PostListDTO getPostListForMyPage(String username, String keyword, int pageNumber) {
        log.info("[class] PostServiceImpl - [method] getPostList -  - page : {} | keyword : {}", pageNumber, keyword);
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(Sort.Order.desc("postId")));
        Page<Post> postPage = null;

        if (keyword == null || keyword.isEmpty()) {
            // 키워드가 없을 경우 전체 목록 반환
            keyword = "";
            postPage = postRepository.findByUser_Username(username, pageable);
        } else {
            // 키워드가 있을 경우 제목에서 키워드를 검색
            postPage = postRepository.findByUser_UsernameAndTitleContaining(username, keyword, pageable);
        }

        return new PostListDTO(postPage, keyword);
    }

    private PostSimpleDTO convertPostToPostSimpleDTO(Post post, boolean conetent_is_required) {
        String content = conetent_is_required ? post.getContent() : "";

        Set<Comment> commentSet = post.getCommentSet();
        int commentSize = (commentSet == null || commentSet.isEmpty()) ? 0 : commentSet.size();
        return PostSimpleDTO.builder()
            .postId(post.getPostId())
            .title(post.getTitle())
            .content(content)
            .published(post.isPublished())
            .username(post.getUser().getUsername())
            .createdAt(post.getCreatedAt())
            .commentSize(commentSize)
            .build();
    }

    // 그냥 생성자로 처리할까 싶었는데, commentPage 불러 오려면 서비스가 필요하고, 고작 생성자에 서비스까지 써주기에는 아까움
    // createPost와 updatePost는 commentPageNum을 0으로 고정
    private PostDetailDTO convertPostToPostDetailDTO(Post post, int commentPageNum) {
        Page<Comment> commentPage = commentService.getCommentListByPostId(post.getPostId(), commentPageNum);
        if(commentPage.isEmpty() || commentPage == null) {
            return PostDetailDTO.builder()
                .postId(post.getPostId())
                .username(post.getUser().getUsername())
                .title(post.getTitle())
                .content(post.getContent())
                .published(post.isPublished())
                .createdAt(post.getCreatedAt())
                .currentPageNum(0)
                .currentPageSize(0)
                .listSize(0)
                .totalPageNum(0)
                .build();
        } else {
            return PostDetailDTO.builder()
                    .postId(post.getPostId())
                    .username(post.getUser().getUsername())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .published(post.isPublished())
                    .createdAt(post.getCreatedAt())
                    .commentList(commentService.convertCommentPageToCommentSimpleDTOList(commentPage))
                    .currentPageNum(commentPage.getNumber())
                    .currentPageSize(commentPage.getNumberOfElements())
                    .listSize(commentPage.getTotalElements())
                    .totalPageNum(commentPage.getTotalPages())
                    .build();
        }
    }

}
