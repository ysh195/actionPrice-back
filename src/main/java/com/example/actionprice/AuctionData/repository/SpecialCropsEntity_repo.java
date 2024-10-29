package com.example.actionprice.AuctionData.repository;

import com.example.actionprice.AuctionData.entity.AuctionEntity_ani;
import com.example.actionprice.AuctionData.entity.AuctionEntity_fruit;
import com.example.actionprice.AuctionData.entity.AuctionEntity_specialCrop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialCropsEntity_repo extends JpaRepository<AuctionEntity_specialCrop, Long> {
    List<AuctionEntity_specialCrop> findByMiddle(String large);
    List<AuctionEntity_specialCrop> findBySmall(String large, String middle);
    List<AuctionEntity_specialCrop> findbyProductRank(String large,String middle,String small);
    List<AuctionEntity_specialCrop> findByPrice(String large,String middle,String small,String rank);



}
