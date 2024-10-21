package com.example.actionprice.customerService.post;

import com.example.actionprice.user.User;
import com.example.actionprice.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

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
}
