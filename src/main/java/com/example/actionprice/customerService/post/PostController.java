package com.example.actionprice.customerService.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @GetMapping(value = "/{id}/detail")
    public PostDetailDTO goDetailPost(@PathVariable Integer postId) {
        return postService.getDetailPost(postId);
    }

    @GetMapping(value = "/{id}/update")
    public ResponseEntity<Map<String, Map<String, String>>> goUpdatePost(@PathVariable Integer postId) {
        PostDetailDTO postDetailDTO = postService.getDetailPost(postId);
        Map<String, String> data = new HashMap<>();
        data.put("title", postDetailDTO.getTitle());
        data.put("content", postDetailDTO.getContent());
        Map<String, Map<String, String>> response = Map.of("data", data);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/create" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Map<String,String>>> createPost(@Validated(PostForm.PostCreateGroup.class) @RequestBody PostForm form){
        log.info("[class] PostController - [method] createPost - username : {}", form.getUsername());
        String message = postService.createPost(form);
        Map<String,String> data = Map.of("message", message);
        Map<String, Map<String,String>> response = Map.of("data", data);
        log.info("[class] PostController - [method] createPost - Success");
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/{id}/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Map<String, String>>> updatePost(@PathVariable("id") Integer id, @RequestBody @Validated(PostForm.PostCreateGroup.class) PostForm form) {
        String message = postService.updatePost(id, form);
        Map<String, Map<String, String>> response = new HashMap<>();
        response.put("data", Collections.singletonMap("message", message)); //Collections.singletonMap 맵 객체중 하나만 저장
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/{id}/delete" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Map<String, String>>> deletePost(@PathVariable("id") Integer id) {
        String message = postService.deletePost(id);
        Map<String, Map<String, String>> response = new HashMap<>();
        response.put("data", Collections.singletonMap("message", message));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public Page<Post> getPostList(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(required = false) String keyword) {
        Page<Post> postList = postService.getPostList(page, keyword);
        return postList;
    }

}
