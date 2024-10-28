package com.example.actionprice.AuctionData.repository;

import com.example.actionprice.AuctionData.entity.AuctionEntity_ani;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AniEntity_repo extends JpaRepository<AuctionEntity_ani, Long> {
    List<AuctionEntity_ani> findByLarge(String large);
}
