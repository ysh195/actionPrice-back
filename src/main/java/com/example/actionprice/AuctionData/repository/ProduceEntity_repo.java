package com.example.actionprice.AuctionData.repository;

import com.example.actionprice.AuctionData.entity.AuctionEntity_ani;
import com.example.actionprice.AuctionData.entity.AuctionEntity_produce;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduceEntity_repo extends JpaRepository<AuctionEntity_produce, Long> {
}
