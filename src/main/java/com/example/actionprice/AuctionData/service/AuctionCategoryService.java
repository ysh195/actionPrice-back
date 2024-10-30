package com.example.actionprice.AuctionData.service;

import com.example.actionprice.AuctionData.dto.MiddleCategoryDTO;
import com.example.actionprice.AuctionData.dto.PriceDTO;
import com.example.actionprice.AuctionData.dto.ProductRankDTO;
import com.example.actionprice.AuctionData.dto.SmallCategoryDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.OptionalDouble;

public interface AuctionCategoryService {

    // 대분류에 따른 중분류 조회
    MiddleCategoryDTO getMiddleCategory(String large);

    // 대분류와 중분류에 따른 소분류 조회
    SmallCategoryDTO getSmallCategory(String large, String middle);

    // 대분류, 중분류, 소분류에 따른 품목 등급 조회
    ProductRankDTO getProductRankCategory(String large, String middle, String small);

    // 대분류, 중분류, 소분류, 등급에 따른 평균 가격 조회
    PriceDTO getAveragePrice(String large, String middle, String small, String rank, LocalDate startDate, LocalDate endDate);

}
