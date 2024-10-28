package com.example.actionprice.user.favorite;

import java.util.List;

public interface FavoriteService {

  boolean createFavorite(
      String large,
      String middle,
      String small,
      String logined_username,
      String favorite_name
  );

  boolean deleteFavorite(String logined_username, String favorite_name);

  List<FavoriteSimpleDTO> getFavoriteList(String logined_username);
}
