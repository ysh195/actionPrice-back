package com.example.actionprice.myPage;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface MyPageService {
    Map<String, String> getPersonalInfo(String username, HttpServletRequest request);
    boolean deleteUser(String username, HttpServletRequest request);
}
