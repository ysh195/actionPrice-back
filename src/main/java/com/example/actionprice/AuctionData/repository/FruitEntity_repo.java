package com.example.actionprice.AuctionData.repository;

import com.example.actionprice.AuctionData.entity.AuctionEntity_ani;
import com.example.actionprice.AuctionData.entity.AuctionEntity_foodCrops;
import com.example.actionprice.AuctionData.entity.AuctionEntity_fruit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FruitEntity_repo extends JpaRepository<AuctionEntity_fruit, Long> {
    List<AuctionEntity_fruit> findByMiddle(String large);
    List<AuctionEntity_fruit> findBySmall(String large, String middle);
    List<AuctionEntity_fruit> findbyProductRank(String large,String middle,String small);
    List<AuctionEntity_fruit> findByPrice(String large,String middle,String small,String rank);



}
