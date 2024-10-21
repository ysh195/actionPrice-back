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
    public String createPost(String username,PostForm form) {
        User user = userRepository.findById(username).orElse(null);
        if(user == null) {
            throw new RuntimeException();
        }
        Post post = Post.builder()
                .user(user)
                .title(form.getTitle())
                .content(form.getContent())
                .published(form.isPublished())
                .build();
        user.addPost(post);

        userRepository.save(user);
        postRepository.save(post);
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
        return postRepository.findByTitleContaining(keyword, pageable);
    }

//        public Page<Post> findPosts(String keyword, Pageable pageable) {
//        // keyword가 null이 아니고 빈 문자열이 아닌 경우에만 검색 조건을 추가
//        Specification<Post> spec = (root, query, cb) -> cb.conjunction(); // 기본적인 조건 (항상 true)
//
//        //keyword가 null이거나 공백 문자열일 경우 Specification 조건이 추가되지 않음 즉, 조건 없이 모든 게시물이 페이징된 형태로 반환
//        if (keyword != null && !keyword.trim().isEmpty()) {
//            spec = spec.and(PostSpecifications.hasTitle(keyword)
//                    .or(PostSpecifications.hasUserName(keyword)));
//        }
//        return postRepository.findAll(spec, pageable);
//    }


}
