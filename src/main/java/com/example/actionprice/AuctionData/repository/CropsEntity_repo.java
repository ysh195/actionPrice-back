package com.example.actionprice.AuctionData.repository;

import com.example.actionprice.AuctionData.entity.AuctionEntity_ani;
import com.example.actionprice.AuctionData.entity.AuctionEntity_crops;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropsEntity_repo extends JpaRepository<AuctionEntity_crops, Long> {
}
