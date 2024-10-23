package com.example.actionprice.common;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
@Log4j
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

  // 이미지들을 static에 다 저장하고 경로를 입력해야 함
  private final String[] imagePaths = {
      "static/image/meat.jpg",
      "static/image/fish.jpg"
  };

  @Override
  public Map<String, String> fetchImages() throws IOException {
    Map<String, String> images = new HashMap<>();

    for (String imagePath : imagePaths) {
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
