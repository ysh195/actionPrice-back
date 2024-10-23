package com.example.actionprice;

import com.example.actionprice.home.HomeService;
import java.io.IOException;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 
 * @author : 연상훈
 * @created : 2024-10-11 오후 8:29
 * @updated 2024-10-17 오후 7:51
 * @info : 불필요한 테스트는 바로 삭제
 */
// TODO 데이터 저장 기능은 논의 후 구현
@SpringBootTest(classes = {ActionPriceApplication.class})
@Log4j2
class ActionPriceApplicationTests {

  @Autowired
  HomeService homeService;

  @Test
  public void contextLoads() throws IOException {
    Map<String, String> map = homeService.fetchImages();
    System.out.println(map.get("cookie"));
  }

}
