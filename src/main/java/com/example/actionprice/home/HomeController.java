package com.example.actionprice.home;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 홈으로 이동하는 요청과 경로를 명확히 하기 위한 컨트롤러입니다.
 * @author 연상훈
 * @created 2024-10-08 오후 4:09
 */
@RestController
@Log4j2
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/")
    public Map<String, Object> fetchHomeImages() {
        log.info("fetch images");
        Map<String, String> images = null;

        try {
            images = homeService.fetchImages();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load images");
        }

        return Map.of("images", images);
    }

}
