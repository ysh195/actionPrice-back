package com.example.actionprice.myPage;

import com.example.actionprice.customerService.post.PostDetailDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface MyPageService {
    Map<String, String> getPersonalInfo(String username, HttpServletRequest request);
    boolean deleteUser(String username, HttpServletRequest request);
    List<PostDetailDTO> getMyPosts(String username, String keyword, int pageNum, HttpServletRequest request);
}
