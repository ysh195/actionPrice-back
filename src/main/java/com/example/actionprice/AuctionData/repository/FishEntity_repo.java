package com.example.actionprice.AuctionData.repository;

import com.example.actionprice.AuctionData.entity.AuctionEntity_ani;
import com.example.actionprice.AuctionData.entity.AuctionEntity_fish;
import com.example.actionprice.AuctionData.entity.AuctionEntity_foodCrops;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FishEntity_repo extends JpaRepository<AuctionEntity_fish, Long> {
    List<AuctionEntity_fish> findByMiddle(String large);
    List<AuctionEntity_fish> findBySmall(String large,String middle);
    List<AuctionEntity_fish> findbyProductRank(String large, String middle, String small);
    List<AuctionEntity_fish> findByPrice(String middle,String large,String small,String rank);
}
