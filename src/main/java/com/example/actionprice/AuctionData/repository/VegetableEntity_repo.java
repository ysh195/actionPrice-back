package com.example.actionprice.AuctionData.repository;

import com.example.actionprice.AuctionData.entity.AuctionEntity_vegetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VegetableEntity_repo extends JpaRepository<AuctionEntity_vegetable, Long> {
    List<AuctionEntity_vegetable> findByLarge(String large);
}
