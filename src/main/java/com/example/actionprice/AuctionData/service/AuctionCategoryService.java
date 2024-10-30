package com.example.actionprice.AuctionData.service;

import com.example.actionprice.AuctionData.dto.CategoryResultDTO;
import com.example.actionprice.AuctionData.dto.CategoryDTO;

import java.time.LocalDate;

public interface AuctionCategoryService {

    // 대분류에 따른 중분류 조회
    CategoryDTO getMiddleCategory(String large);

    // 대분류와 중분류에 따른 소분류 조회
    CategoryDTO getSmallCategory(String large, String middle);

    // 대분류, 중분류, 소분류에 따른 품목 등급 조회
    CategoryDTO getProductRankCategory(String large, String middle, String small);

    // 대분류, 중분류, 소분류, 등급에 따른 평균 가격 조회
    CategoryResultDTO getAveragePrice(String large, String middle, String small, String rank, LocalDate startDate, LocalDate endDate);

}
