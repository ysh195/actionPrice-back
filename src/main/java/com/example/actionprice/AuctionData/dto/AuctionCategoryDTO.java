package com.example.actionprice.AuctionData.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AuctionCategoryDTO {

    private Map<String, List<String>> middleCategories; // 중분류와 제품명을 담는 맵
    private double averagePrice; // 평균 가격

    public AuctionCategoryDTO(Map<String, List<String>> middleCategories, double averagePrice) {
        this.middleCategories = middleCategories;
        this.averagePrice = averagePrice;
    }
}
