package com.example.actionprice.home;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/**
 * 홈페이지 화면에 출력될 정보들을 전달하는 서비스
 * @author 연상훈
 * @created 2024-11-28 오후 5:33
 * @info 이미지를 프론트에서 랜더링하는 건 오래 걸리니까 백에서 전달
 * @info 단순히 이미지가 캐싱되는 위치가 레디스일 뿐이라서 레디스와의 관계성이 약함. 따라서 레디스 패키지에는 넣지 않음
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class HomeServiceImpl implements HomeService {

  // 이미지들을 static에 다 저장하고 경로를 입력해야 함
  // 홈페이지에 반환할 이미지들의 경로
  private final String[] imagePaths = {
          "static/image/crop.jpg",
          "static/image/fish.jpg",
          "static/image/fruit.jpg",
          "static/image/meat.jpg",
          "static/image/scrop.jpg",
          "static/image/ve.jpg",
  };

  /**
   * 홈페이지에서 사용할 이미지를 인코딩해서 반환하는 fetch 메서드
   * @author 연상훈
   * @created 2024-11-08 오전 11:05
   * @updated 2024-11-28 오후 5:34 [연상훈] : 레디스에 반환값들을 캐싱해둠
   * @info 홈페이지로 이동할 때마다 이미지를 가져와야 하고, 가져올 이미지는 고정되어 있으니, 그냥 캐싱해두고 씀
   * @info 캐싱 유효시간은 현재 10분
   */
  @Cacheable(value = "imagesCache", key = "'homeImages'")
  @Override
  public Map<String, String> fetchImages() throws IOException {
    log.info("Fetching images - 이게 뜨면 캐싱 안 된 것");
    Map<String, String> images = new HashMap<>();

    for (String imagePath : imagePaths) {
      log.info("image path: " + imagePath);
      ClassPathResource image = new ClassPathResource(imagePath);
      byte[] imageBytes = Files.readAllBytes(image.getFile().toPath());
      String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);

      // 마지막 슬래시("/") 뒤부터 맨 뒤의 점(".") 까지
      String imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
      imageName = imageName.substring(0, imageName.lastIndexOf("."));

      images.put(imageName, base64Image);
    }

    return images;
  }
}
