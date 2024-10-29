package com.example.actionprice.AuctionData.repository;

import com.example.actionprice.AuctionData.entity.AuctionEntity_ani;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AniEntity_repo extends JpaRepository<AuctionEntity_ani, Long> {
    List<AuctionEntity_ani> findByMiddle(String large);
    List<AuctionEntity_ani> findBySmall(String large,String middle);
    List<AuctionEntity_ani> findbyProductRank(String large,String middle,String small);
    List<AuctionEntity_ani> findByPrice(String large,String middle,String small,String rank);
}
