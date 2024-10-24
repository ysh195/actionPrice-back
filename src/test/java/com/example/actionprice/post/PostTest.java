package com.example.actionprice.post;

import com.example.actionprice.customerService.post.PostController;
import com.example.actionprice.customerService.post.PostService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class PostTest {

    @Autowired
    PostService postService;

    @Autowired
    PostController postController;

    @Test
    public void test() {
        System.out.println(postController.goDetailPost(1).toString());
    }


}
