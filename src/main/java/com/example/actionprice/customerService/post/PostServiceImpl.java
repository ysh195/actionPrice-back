package com.example.actionprice.customerService.post;

import com.example.actionprice.user.User;
import com.example.actionprice.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional //실패시 다같이 실패가 뜸
@Log4j2
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public String createPost(PostForm form) {
        User user = userRepository.findById(form.getUsername()).orElse(null);
        if(user == null) {
            throw new RuntimeException();
        }
        Post post = Post.builder()
                .user(user)
                .title(form.getTitle())
                .content(form.getContent())
                .published(form.isPublished())
                .build();
        postRepository.save(post);

        user.addPost(post);
        userRepository.save(user);
        return "";

    }

    @Override
    public String updatePost(Integer postId, PostForm form) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Id 에러  " + postId));
        Post updatedPost = Post.builder()
                .postId(post.getPostId())
                .user(post.getUser())
                .title(form.getTitle())
                .content(form.getContent())
                .published(post.isPublished())
                .build();

        post = updatedPost;
        postRepository.save(post);
        return "";
    }


    @Override
    public String deletePost(Integer postId) {

        postRepository.deleteById(postId);
        return "";
    }

    @Override
    public Page<Post> getPostList(int page, String keyword) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("postId")));
        // 키워드가 없을 경우 전체 목록 반환
        if (keyword == null || keyword.isEmpty()) {
            return postRepository.findAll(pageable);
        }
        // 키워드가 있을 경우 제목에서 키워드를 검색
        return postRepository.findByKeywordContaining(keyword, pageable);
    }

    @Override
    public PostDetailDTO getDetailPost(Integer postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> {throw new RuntimeException("the post does not exist");});

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
