package com.example.actionprice.myPage;

import com.example.actionprice.customerService.post.dto.PostDetailDTO;
import com.example.actionprice.customerService.post.dto.PostListDTO;
import com.example.actionprice.exception.AccessTokenException;
import com.example.actionprice.exception.AccessTokenException.TOKEN_ERROR;
import com.example.actionprice.exception.UserNotFoundException;
import com.example.actionprice.security.jwt.accessToken.AccessTokenService;
import com.example.actionprice.user.UserService;
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

    // request에서 토큰 추출해서 검사하는 건 controller 계층에서 끝내자. request를 서비스까지 끌고 가는 건 좀 요상함
    private final MyPageService myPageService;
    private final AccessTokenService accessTokenService;
    private final UserService userService;

    @GetMapping(value = "/{username}/personalinfo")
    public Map<String, String> goPersonalInfo(@PathVariable("username") String username, HttpServletRequest request) {
        return myPageService.getPersonalInfo(username);
    }

    @GetMapping(value = "/{username}/myposts")
    public PostListDTO goMyPosts(
             @PathVariable("username") String username,
             @RequestParam(required = false, name = "keyword") String keyword,
             @RequestParam(required = false, name = "pageNum", defaultValue = "0") int pageNum,
             HttpServletRequest request)
    {
        checkUserAndRequest(username, request);
        return myPageService.getMyPosts(username, keyword, pageNum);
    }

    @GetMapping(value = "/{username}/wishlist")
    public String goMyWishlist(@PathVariable("username") String username, HttpServletRequest request) {
        checkUserAndRequest(username, request);
        // 메인데이터 카테고리 끝나야 가능해서 나중에 처리함
        return "OK";
    }

    @PostMapping(value = "/{username}/deleteUser")
    public String deleteUser(@PathVariable("username") String username, HttpServletRequest request){
        checkUserAndRequest(username, request);
        myPageService.deleteUser(username);

        return "delete user : success";
    }

    private void checkUserAndRequest(String username, HttpServletRequest request) {
        boolean isUserExists = userService.checkUserExistsWithUsername(username);

        if(!isUserExists){
            throw new UserNotFoundException("user(" + username + ") not found");
        }

        Map<String, Object> payload = accessTokenService.validateAccessTokenInReqeust(request);

        String username_from_token = (String)payload.get("username");

        if (!username_from_token.equals(username)) {
            throw new AccessTokenException(TOKEN_ERROR.MALFORM);
        }
    }
}
