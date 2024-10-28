package com.example.actionprice.AuctionData.repository;

import com.example.actionprice.AuctionData.entity.AuctionEntity_ani;
import com.example.actionprice.AuctionData.entity.AuctionEntity_specialCrop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialCropsEntity_repo extends JpaRepository<AuctionEntity_specialCrop, Long> {
    List<AuctionEntity_specialCrop> findByLarge(String large);
}
