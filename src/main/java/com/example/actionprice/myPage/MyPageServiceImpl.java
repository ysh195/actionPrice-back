package com.example.actionprice.myPage;

import com.example.actionprice.exception.UserNotFoundException;
import com.example.actionprice.security.jwt.accessToken.AccessTokenService;
import com.example.actionprice.user.User;
import com.example.actionprice.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final UserRepository userRepository;
    private final AccessTokenService accessTokenService;

    @Override
    public Map<String, String> getPersonalInfo(String username, HttpServletRequest request) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException("user(username : " + username + ") does not exist"));

        Map<String, Object> payload = accessTokenService.validateAccessTokenInReqeust(request);

        String username_from_token = (String)payload.get("username");

        if (!username_from_token.equals(username)) {
            return null;
        }

        return Map.of("username", username, "email", user.getEmail());
    }

    @Override
    public boolean deleteUser(String username, HttpServletRequest request) {
        User userByUsername = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException("user(username : " + username + ") does not exist"));

        Map<String, Object> payload = accessTokenService.validateAccessTokenInReqeust(request);

        String username_from_token = (String)payload.get("username");

        if (!username_from_token.equals(username)) {
            return false;
        }

        userRepository.delete(userByUsername);

        return true;
    }
}
