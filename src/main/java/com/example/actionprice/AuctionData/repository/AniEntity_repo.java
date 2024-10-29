package com.example.actionprice.AuctionData.repository;

import com.example.actionprice.AuctionData.entity.AuctionEntity_ani;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AniEntity_repo extends JpaRepository<AuctionEntity_ani, Long> {
    List<AuctionEntity_ani> findByLarge(String large);
    List<AuctionEntity_ani> findByLargeAndMiddle(String large,String middle);
    List<AuctionEntity_ani> findByLargeAndMiddleAndProduct_name(String large,String middle,String produce_name);
    List<AuctionEntity_ani> findByLargeAndMiddleAndProduct_nameAndProduct_rank(String large,String middle,String product_name,String product_rank);

    //나머지도 다 수정해야함 오류 고치면 다 적용
}
