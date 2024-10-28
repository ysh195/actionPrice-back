package com.example.actionprice.admin;

import com.example.actionprice.user.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Log4j2
@RequiredArgsConstructor
public class AdminController {

  private final UserService userService;

  @GetMapping("/userList")
  public UserListDTO getUserList(
      @RequestParam(name = "pageNum", defaultValue = "0", required = false) int pageNum,
      @RequestParam(name = "keyword", required = false) String keyword
  ) {
    log.info("[class] AdminController - [method] getUserList - page : {} | keyword : {}", pageNum, keyword);
    return userService.getUserList(keyword, pageNum);
  }

  @PostMapping("/userList/block/{username}")
  public Map<String, Object> blockUser(@PathVariable("username") String selected_username) {
    // 로직은 진짜 간단한데
    // 토큰 관련이라서 관심사를 어디에 설정해야 할 지 고민
    return null;
  }

}
