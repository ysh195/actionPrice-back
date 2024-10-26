package com.example.actionprice.myPage;

import com.example.actionprice.customerService.post.dto.PostDetailDTO;
import com.example.actionprice.customerService.post.PostService;
import com.example.actionprice.customerService.post.dto.PostListDTO;
import com.example.actionprice.exception.UserNotFoundException;
import com.example.actionprice.user.User;
import com.example.actionprice.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

    private final UserRepository userRepository;
    private final PostService postService;

    @Override
    public Map<String, String> getPersonalInfo(String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new UserNotFoundException(username));

        return Map.of("username", username, "email", user.getEmail());
    }

    @Override
    public void deleteUser(String username) {

        userRepository.deleteById(username);
    }

    @Override
    public PostListDTO getMyPosts(String username, String keyword, int pageNum) {

        return postService.getPostListForMyPage(username, keyword, pageNum);
    }

}
