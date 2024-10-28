package com.example.actionprice.AuctionData.repository;

import com.example.actionprice.AuctionData.entity.AuctionEntity_ani;
import com.example.actionprice.AuctionData.entity.AuctionEntity_foodCrops;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodCropsEntity_repo extends JpaRepository<AuctionEntity_foodCrops, Long> {
    List<AuctionEntity_foodCrops> findByLarge(String large);
}
