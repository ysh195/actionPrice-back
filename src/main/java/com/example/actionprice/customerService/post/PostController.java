package com.example.actionprice.customerService.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.repository.query.Param;
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

    @GetMapping(value = "/{id}")
    public ResponseEntity<Map<String, Map<String,String>>> goDetailPost(@PathVariable Integer postId) {
        String url = String.format("/api/post/%s", postId);
        Map<String,String> data = Map.of("url", url);
        Map<String, Map<String,String>> response = Map.of("data", data);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}/update")
    public ResponseEntity<Map<String, Map<String,String>>> goUpdatePost(@PathVariable Integer postId) {
        String url = String.format("/api/post/%s/update", postId);
        Map<String,String> data = Map.of("url", url);
        Map<String, Map<String,String>> response = Map.of("data", data);
        return ResponseEntity.ok(response);
    }


    @PostMapping(value = "/createPost" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Map<String,String>>> createPost(@Validated(PostForm.PostCreateGroup.class) @RequestBody PostForm form, Principal principal){
       String message = postService.createPost(principal.getName(), form);
       Map<String,String> data = Map.of("message", message);
       Map<String, Map<String,String>> response = Map.of("data", data);
       return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/{id}/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Map<String, String>>> updatePost(@PathVariable("id") Integer id, @RequestBody @Validated(PostForm.PostCreateGroup.class) PostForm form) {
        String message = postService.updatePost(id, form);
        Map<String, Map<String, String>> response = new HashMap<>();
        response.put("data", Collections.singletonMap("message", message)); //Collections.singletonMap 맵 객체중 하나만 저장
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/{id}/deletePost" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Map<String, String>>> deletePost(@PathVariable("id") Integer id) {
        String message = postService.deletePost(id);
        Map<String, Map<String, String>> response = new HashMap<>();
        response.put("data", Collections.singletonMap("message", message));
        return ResponseEntity.ok(response);
    }

}
