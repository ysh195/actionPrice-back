package com.example.actionprice.customerService.post;

import com.example.actionprice.customerService.post.dto.PostDetailDTO;
import com.example.actionprice.customerService.post.dto.PostListDTO;
import com.example.actionprice.customerService.post.dto.PostSimpleDTO;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;

    @PostMapping(value = "/create" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public Integer createPost(@RequestBody @Validated(PostForm.PostCreateGroup.class) PostForm form){
        log.info("[class] PostController - [method] createPost - username : {}", form.getUsername());
        return postService.createPost(form).getPostId();
    }

    @GetMapping(value = "/{id}/detail")
    public PostDetailDTO goDetailPost(
        @PathVariable("id") Integer postId,
        @RequestParam(name = "commentPageNum", defaultValue = "0", required = false) int commentPageNum
    ) {
        log.info("goDetailPost");
        return postService.getDetailPost(postId, commentPageNum);
    }

    @GetMapping(value = "/{id}/update")
    public PostSimpleDTO goUpdatePost(@PathVariable("id") Integer postId) {
        log.info("goUpdatePost");

        return postService.goUpdatePost(postId);
    }

    @PostMapping(value = "/{id}/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public PostDetailDTO updatePost(@PathVariable("id") Integer id, @RequestBody @Validated(PostForm.PostUpdateGroup.class) PostForm form) {
        return postService.updatePost(id, form);
    }

    @PostMapping(value = "/{id}/delete" ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public String deletePost(@PathVariable("id") Integer id, @RequestBody Map<String, Object> requestBody) {
        String logined_username = (String)requestBody.get("logined_username");
        if((logined_username == null) || (logined_username.isEmpty())) {
            return "delete failed";
        }

        log.info("[class] PostController - [method] deletePost - id : {} | username : {}", id, logined_username);
        postService.deletePost(id, logined_username);
        return "delete successful";
    }

    @GetMapping("/list")
    public PostListDTO getPostList(
        @RequestParam(name = "pageNum", defaultValue = "0", required = false) int pageNum,
        @RequestParam(name = "keyword", required = false) String keyword
    ) {
        log.info("[class] PostController - [method] getPostList - page : {} | keyword : {}", pageNum, keyword);
        return postService.getPostList(pageNum, keyword);
    }

}
