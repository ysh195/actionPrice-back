package com.example.actionprice.AuctionData.repository;

import com.example.actionprice.AuctionData.entity.AuctionEntity_ani;
import com.example.actionprice.AuctionData.entity.AuctionEntity_specialCrop;
import com.example.actionprice.AuctionData.entity.AuctionEntity_vegetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VegetableEntity_repo extends JpaRepository<AuctionEntity_vegetable, Long> {
    List<AuctionEntity_vegetable> findByMiddle(String large);
    List<AuctionEntity_vegetable> findBySmall(String large, String middle);
    List<AuctionEntity_vegetable> findbyProductRank(String large, String middle, String small);
    List<AuctionEntity_vegetable> findByPrice(String large,String middle,String small,String rank);



}
