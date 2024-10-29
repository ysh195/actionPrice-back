package com.example.actionprice.AuctionData.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SmallCategoryDTO {

    private String large;
    private String middle;
    private List<String> smallCategories;

}
