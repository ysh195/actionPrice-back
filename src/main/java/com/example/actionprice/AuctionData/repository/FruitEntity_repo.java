package com.example.actionprice.AuctionData.repository;

import com.example.actionprice.AuctionData.entity.AuctionEntity_ani;
import com.example.actionprice.AuctionData.entity.AuctionEntity_fruit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FruitEntity_repo extends JpaRepository<AuctionEntity_fruit, Long> {
    List<AuctionEntity_fruit> findByLarge(String large);
}
