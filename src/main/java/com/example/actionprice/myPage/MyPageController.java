package com.example.actionprice.myPage;

import com.example.actionprice.customerService.post.dto.PostListDTO;
import com.example.actionprice.exception.AccessTokenException;
import com.example.actionprice.exception.AccessTokenException.TOKEN_ERROR;
import com.example.actionprice.security.jwt.accessToken.AccessTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author 연상훈
 * @created 2024-10-27 오후 3:30
 * @info
 * request 내 토큰 추출 및 검사는
 * service의 관심사에 맞지 않는 것 같아서 controller에서 해결
 * 그리고 그렇게 하니까 양쪽 따 훨씬 깔끔함
 */
@RestController
@RequestMapping("/api/mypage")
@Log4j2
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final AccessTokenService accessTokenService;

    /**
     * 자신의 MyPage의 개인정보 확인 페이지로 이동하는 기능
     * @param username : MyPage의 주인의 username
     * @param request reqeust 내부의 access token을 검사하기 위해 받음
     * @author 연상훈
     * @created 2024-10-27 오후 3:34
     */
    @GetMapping(value = "/{username}/personalinfo")
    public Map<String, String> goPersonalInfo(
        @PathVariable("username") String username,
        HttpServletRequest request
    ) {
        checkUserAndRequest(username, request);
        return myPageService.getPersonalInfo(username);
    }
    
    /**
     * 자신의 MyPage의 게시글 확인 페이지로 이동하는 기능
     * @param username : MyPage의 주인의 username
     * @param request reqeust 내부의 access token을 검사하기 위해 받음
     * @author 연상훈
     * @created 2024-10-27 오후 3:34
     */
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

    /**
     * 자신의 MyPage의 즐겨찾기 목록 확인 페이지로 이동하는 기능
     * @param username : MyPage의 주인의 username
     * @param request reqeust 내부의 access token을 검사하기 위해 받음
     * @author 연상훈
     * @created 2024-10-27 오후 3:34
     */
    @GetMapping(value = "/{username}/wishlist")
    public String goMyWishlist(@PathVariable("username") String username, HttpServletRequest request) {
        checkUserAndRequest(username, request);
        // 메인데이터 카테고리 끝나야 가능해서 나중에 처리함
        return "OK";
    }

    /**
     * 자신의 MyPage의 회원탈퇴(user 삭제) 기능
     * @param username : MyPage의 주인의 username
     * @param request reqeust 내부의 access token을 검사하기 위해 받음
     * @author 연상훈
     * @created 2024-10-27 오후 3:34
     */
    @PostMapping(value = "/{username}/deleteUser")
    public String deleteUser(@PathVariable("username") String username, HttpServletRequest request){
        checkUserAndRequest(username, request);
        myPageService.deleteUser(username);

        return "delete user : success";
    }

    /**
     * 이동하고자 하는 MyPage의 주인과 access token에 저장된 사용자의 이름을 대조하는 메서드
     * @param username : MyPage의 주인의 username
     * @param request reqeust 내부의 access token을 검사하기 위해 받음
     * @author 연상훈
     * @created 2024-10-27 오후 3:36
     * @info 
     */
    private void checkUserAndRequest(String username, HttpServletRequest request) {
        Map<String, Object> payload = accessTokenService.validateAccessTokenInReqeust(request);

        String username_from_token = (String)payload.get("username");

        if (!username_from_token.equals(username)) {
            throw new AccessTokenException(TOKEN_ERROR.MALFORM);
        }
    }
}
