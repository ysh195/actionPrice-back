package com.example.actionprice.myPage;

import com.example.actionprice.customerService.post.PostDetailDTO;
import com.example.actionprice.security.jwt.accessToken.AccessTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public List<PostDetailDTO> goMyPosts(
             @PathVariable("username") String username,
             @RequestParam(required = false, name = "keyword") String keyword,
             @RequestParam(name = "pageNum") int pageNum,
             HttpServletRequest request)
    {
        return myPageService.getMyPosts(username, keyword, pageNum, request);
    }

    @GetMapping(value = "/{username}/wishlist")
    public String goMyWishlist(@PathVariable("username") String username, HttpServletRequest request) {
        // 메인데이터 카테고리 끝나야 가능해서 나중에 처리함
        return "OK";
    }

    @PostMapping(value = "/{username}/deleteUser")
    public String deleteUser(@PathVariable("username") String username, HttpServletRequest request){

        boolean isSuccess = myPageService.deleteUser(username, request);

        return isSuccess ? "delete user : success" : "delete user : failed";
    }
}
