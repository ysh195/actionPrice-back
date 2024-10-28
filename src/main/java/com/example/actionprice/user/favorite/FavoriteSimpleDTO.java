package com.example.actionprice.user.favorite;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class FavoriteSimpleDTO {
  private Integer favoriteId;
  private String favoriteName;
  private String favoriteURL;
  private String favorite_ownerS_username;
}
