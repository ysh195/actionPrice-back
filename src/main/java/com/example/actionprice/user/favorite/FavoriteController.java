package com.example.actionprice.user.favorite;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// return을 responseEntity로 해서 에러 처리도 같이 해버릴까?
/**
 * 즐겨찾기 컨트롤러
 * @author 연상훈
 * @created 2024-10-28 오후 2:12
 * @info update 기능 없이, create와 delete만 있을 거임.
 */
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/auctiondata") // 상황에 따라 많이 바꿔야 할 수 있음
public class FavoriteController {

  private final FavoriteService favoriteService;

  // principal을 쓸까 그냥 요청으로 받을까
  @PostMapping("/favorite")
  public String createFavorite(
      @RequestParam(name = "large", required = false, defaultValue = "") String large,
      @RequestParam(name = "middle", required = false, defaultValue = "") String middle,
      @RequestParam(name = "small", required = false, defaultValue = "") String small,
      @RequestBody Map<String, String> requestBody
  ){
    String logined_username = requestBody.get("logined_username");
    if (logined_username == null || logined_username.isEmpty()) {
      log.error("logined_username is null or empty");
      return "logined_username is null or empty";
    }

    String favorite_name = requestBody.get("favorite_name");
    if (favorite_name == null || favorite_name.isEmpty()) {
      log.error("favorite_name is null or empty");
      return "favorite_name is null or empty";
    }

    boolean result = favoriteService.createFavorite(
        large,
        middle,
        small,
        logined_username,
        favorite_name
    );

    return result ? "create favorite : success" : "create favorite : failed";
  }

  @PostMapping("/favorite/delete")
  public String deleteFavorite(@RequestBody Map<String, String> requestBody){
    String logined_username = requestBody.get("logined_username");
    if (logined_username == null || logined_username.isEmpty()) {
      log.error("logined_username is null or empty");
      return "logined_username is null or empty";
    }

    String favorite_name = requestBody.get("favorite_name");
    if (favorite_name == null || favorite_name.isEmpty()) {
      log.error("favorite_name is null or empty");
      return "favorite_name is null or empty";
    }

    boolean result = favoriteService.deleteFavorite(logined_username, favorite_name);

    return result ? "delete favorite : success" : "delete favorite : failed";
  }

  @GetMapping("/favorite/getlist")
  public Map<String, Object> getFavoriteList(@RequestBody Map<String, String> requestBody){
    String logined_username = requestBody.get("logined_username");
    if (logined_username == null || logined_username.isEmpty()) {
      log.error("logined_username is null or empty");
      return Map.of("favoriteList", "no user, no list");
    }

    List<FavoriteSimpleDTO> favoriteList = favoriteService.getFavoriteList(logined_username);

    return Map.of("favoriteList", favoriteList, "username", logined_username);
  }

}
