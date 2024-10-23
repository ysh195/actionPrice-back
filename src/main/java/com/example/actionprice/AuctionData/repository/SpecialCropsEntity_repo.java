package com.example.actionprice.AuctionData.repository;

import com.example.actionprice.AuctionData.entity.AuctionEntity_ani;
import com.example.actionprice.AuctionData.entity.AuctionEntity_specialCrop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialCropsEntity_repo extends JpaRepository<AuctionEntity_specialCrop, Long> {
}
