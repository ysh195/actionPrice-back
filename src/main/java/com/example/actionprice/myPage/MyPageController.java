package com.example.actionprice.myPage;

import com.example.actionprice.customerService.post.Post;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/mypage")
@Log4j2
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping(value = "/{username}/personalinfo")
    public Map<String, String> goPersonalInfo(@PathVariable("username") String username, HttpServletRequest request) {
        return myPageService.getPersonalInfo(username, request);
    }

    @GetMapping(value = "/{username}/myposts")
    public Set<Post> goMyPosts(
             @PathVariable("username") String username,
             @RequestParam(required = false, name = "keyword") String keyword,
             @RequestParam(name = "pageNum") int pageNum,
             HttpServletRequest request)
    {
        return null;
    }

    @PostMapping(value = "/{username}/deleteUser")
    public String deleteUser(@PathVariable("username") String username, HttpServletRequest request){

        boolean isSuccess = myPageService.deleteUser(username, request);

        return isSuccess ? "delete user : success" : "delete user : failed";
    }
}
