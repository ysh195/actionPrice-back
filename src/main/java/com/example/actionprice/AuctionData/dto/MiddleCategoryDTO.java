package com.example.actionprice.AuctionData.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MiddleCategoryDTO {

    private String large;
    private List<String> middleCategories;

}
