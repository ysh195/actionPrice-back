package com.example.actionprice.customerService.post;

import com.example.actionprice.exception.PostNotFoundException;
import com.example.actionprice.exception.UserNotFoundException;
import com.example.actionprice.user.User;
import com.example.actionprice.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional //실패시 다같이 실패가 뜸
@Log4j2
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public PostDetailDTO createPost(PostForm form) {
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

        return convertPostToPostDetailDTO(post);

    }

    @Override
    public PostDetailDTO updatePost(Integer postId, PostForm form) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("post(" + postId + ") does not exist"));

        post.setTitle(form.getTitle());
        post.setContent(form.getContent());

        post = postRepository.save(post);

        return convertPostToPostDetailDTO(post);
    }

    @Override
    public void deletePost(Integer postId, String logined_username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("post(" + postId + ") does not exist"));

        if(logined_username.equals(post.getUser().getUsername())) {
            log.info("you are not the writer");
            return;
        }

        postRepository.delete(post);
    }

    @Override
    public List<PostDetailDTO> getPostList(int page, String keyword) {
        log.info("[class] PostServiceImpl - [method] getPostList -  - page : {} | keyword : {}", page, keyword);
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("postId")));
        Page<Post> postPage = null;

        if (keyword == null || keyword.isEmpty()) {
            // 키워드가 없을 경우 전체 목록 반환
            postPage = postRepository.findAll(pageable);
        } else {
            // 키워드가 있을 경우 제목에서 키워드를 검색
            postPage = postRepository.findByTitleContainingOrUser_UsernameContaining(keyword, keyword, pageable);
        }

        List<PostDetailDTO> postList = postPage.getContent()
                .stream()
                .map(post -> convertPostToPostDetailDTO(post))
                .collect(Collectors.toList());

        return postList;
    }

    @Override
    public PostDetailDTO getDetailPost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("post(" + postId + ") does not exist"));

        return convertPostToPostDetailDTO(post);
    }

    @Override
    public List<PostDetailDTO> getPostListForMyPage(String username, String keyword, int pageNumber) {
        log.info("[class] PostServiceImpl - [method] getPostList -  - page : {} | keyword : {}", pageNumber, keyword);
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(Sort.Order.desc("postId")));
        Page<Post> postPage = null;

        if (keyword == null || keyword.isEmpty()) {
            // 키워드가 없을 경우 전체 목록 반환
            postPage = postRepository.findByUser_Username(username, pageable);
        } else {
            // 키워드가 있을 경우 제목에서 키워드를 검색
            postPage = postRepository.findByUser_UsernameAndTitleContaining(username, keyword, pageable);
        }

        List<PostDetailDTO> postList = postPage.getContent()
                .stream()
                .map(post -> convertPostToPostDetailDTO(post))
                .collect(Collectors.toList());

        return postList;
    }

    private PostDetailDTO convertPostToPostDetailDTO(Post post) {
        return PostDetailDTO.builder()
                .postId(post.getPostId())
                .username(post.getUser().getUsername())
                .title(post.getTitle())
                .content(post.getContent())
                .published(post.isPublished())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .commentSet(post.getCommentSet())
                .build();
    }

}
