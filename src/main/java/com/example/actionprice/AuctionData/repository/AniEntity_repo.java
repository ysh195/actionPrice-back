package com.example.actionprice.AuctionData.repository;

import com.example.actionprice.AuctionData.entity.AuctionEntity_ani;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AniEntity_repo extends JpaRepository<AuctionEntity_ani, Long> {

    // large 값으로 middle 조회
    List<AuctionEntity_ani> findByLarge(String large);

    // large,middle 값으로 productName 조회
    List<AuctionEntity_ani> findByLargeAndMiddle(String large,String middle);

    // large,middle,productName 값으로 productRank 조회
    List<AuctionEntity_ani> findByLargeAndMiddleAndProductName(String large,String middle,String productName);

    // large,middle,productName,productRank 값으로 price 조회 후 평균 도출
    List<AuctionEntity_ani> findByLargeAndMiddleAndProductNameAndProductRank(String large,String middle,String productName,String productRank);


}
