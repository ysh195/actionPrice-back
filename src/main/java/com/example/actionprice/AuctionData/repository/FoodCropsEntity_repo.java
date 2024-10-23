package com.example.actionprice.AuctionData.repository;

import com.example.actionprice.AuctionData.entity.AuctionEntity_ani;
import com.example.actionprice.AuctionData.entity.AuctionEntity_foodCrops;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodCropsEntity_repo extends JpaRepository<AuctionEntity_foodCrops, Long> {
}
