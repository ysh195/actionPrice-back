package com.example.actionprice.post;

import com.example.actionprice.customerService.post.PostService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class PostTest {

    @Autowired
    PostService postService;


}
