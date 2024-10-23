package com.example.actionprice.AuctionData.repository;

import com.example.actionprice.AuctionData.entity.AuctionEntity_ani;
import com.example.actionprice.AuctionData.entity.AuctionEntity_fruit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FruitEntity_repo extends JpaRepository<AuctionEntity_fruit, Long> {
}
