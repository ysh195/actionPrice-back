package com.example.actionprice.user.favorite;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

  List<Favorite> findByUser_Id(String username);
}
