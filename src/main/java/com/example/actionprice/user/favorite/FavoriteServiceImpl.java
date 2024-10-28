package com.example.actionprice.user.favorite;

import com.example.actionprice.exception.UserNotFoundException;
import com.example.actionprice.user.User;
import com.example.actionprice.user.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

  private final FavoriteRepository favoriteRepository;
  private final UserRepository userRepository;

  @Override
  public boolean createFavorite(
      String large,
      String middle,
      String small,
      String logined_username,
      String favorite_name
  ) {
    User user = userRepository.findById(logined_username)
        .orElseThrow(() -> new UserNotFoundException("user(" +logined_username + ") does not exist"));

    String favoriteUrl = composeFavoriteUrl(large, middle, small);
    if (favoriteUrl.equals("none")){
      return false;
    }

    Favorite favorite = Favorite.builder()
        .favoriteName(favorite_name)
        .favoriteURL(favoriteUrl)
        .user(user)
        .build();

    // 레포지토리에 저장해야만 id가 발급되고, 그래야 user에 추가 가능
    favorite = favoriteRepository.save(favorite);

    user.addFavorite(favorite);
    userRepository.save(user);

    return true;
  }

  // 이건 뭔가 더 효율적인 방법이 있을 것 같은데
  // favorite_name은 중복된 게 있을 수 있어서 이걸로 검색하면 안 됨.
  // 아니면 favorite id로 검색해야 하는데, 어떻게 될 지 몰라서 지금은 일단 이렇게 함
  @Override
  public boolean deleteFavorite(String logined_username, String favorite_name) {

    User user = userRepository.findById(logined_username)
        .orElseThrow(() -> new UserNotFoundException("user(" +logined_username + ") does not exist"));

    // user와 favorite의 관계상 연결이 끊어지면 자동 삭제
    boolean isFavoriteRemoved = user.getFavoriteSet()
        .removeIf(favorite -> favorite.getFavoriteName().equals(favorite_name));

    // 대신 save를 해서 해당 user에 대한 변경 사항을 레포지토리에 반영해줘야 함
    userRepository.save(user);

    return isFavoriteRemoved;
  }

  @Override
  public List<FavoriteSimpleDTO> getFavoriteList(String logined_username) {
    List<Favorite> favorites = favoriteRepository.findByUser_Username(logined_username);
    if (favorites.isEmpty() || favorites == null){
      return null;
    }

    return favorites.stream()
        .map(favorite -> FavoriteSimpleDTO.builder()
            .favoriteId(favorite.getFavoriteId())
            .favoriteName(favorite.getFavoriteName())
            .favoriteURL(favorite.getFavoriteURL())
            .favorite_ownerS_username(favorite.getUser().getUsername())
            .build())
        .toList();
  }

  /**
   * 카테고리 중 이상한 게 없는지 확인하고, favorite url을 구성하는 메서드
   * @author 연상훈
   * @created 2024-10-28 오후 2:32
   * @info large > middle > small 순으로 순서대로 for문을 돌리면서 만약 이상한 카테고리가 있으면 거기서 멈추고, 그 이전의 카테고리를 반환함
   * @info 이상한 게 없다면 현재의 것을 result에 저장하고 다음으로 넘어감
   * @info 따라서 large부터 이상하면 "none"을 반환하고, middle이 이상하면 large를 반환.
   * @info 끝까지 돌면서 아무것도 이상한 게 없었다면 small을 반환
   * @see : 지원되지 않는 카테고리를 요청하지 않는지 체크하는 로직도 필요함
   */
  private String composeFavoriteUrl(String large, String middle, String small){

    if(large == null || large.isEmpty()){
      return "none";
    }

    StringBuilder sb = new StringBuilder().append("http://localhost:3000/api/auctiondata?large=");
    sb.append(large);

    if(middle == null || middle.isEmpty()){
      return sb.toString();
    }

    sb.append("&middle=");
    sb.append(middle);

    if(small == null || small.isEmpty()){
      return sb.toString();
    }

    sb.append("&small=");
    sb.append(small);

    return sb.toString();
  }
}
