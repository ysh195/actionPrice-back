package com.example.actionprice.AuctionData.repository;

import com.example.actionprice.AuctionData.entity.AuctionEntity_ani;
import com.example.actionprice.AuctionData.entity.AuctionEntity_fish;
import com.example.actionprice.AuctionData.entity.AuctionEntity_foodCrops;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodCropsEntity_repo extends JpaRepository<AuctionEntity_foodCrops, Long> {
    List<AuctionEntity_foodCrops> findByMiddle(String large);
    List<AuctionEntity_foodCrops> findBySmall(String large, String middle);
    List<AuctionEntity_foodCrops> findbyProductRank(String large,String middle,String small);
    List<AuctionEntity_foodCrops> findByPrice(String large,String middle,String small,String rank);



}
