package com.example.actionprice.AuctionData.controller;

import com.example.actionprice.AuctionData.dto.MiddleCategoryDTO;
import com.example.actionprice.AuctionData.dto.PriceDTO;
import com.example.actionprice.AuctionData.dto.ProductRankDTO;
import com.example.actionprice.AuctionData.dto.SmallCategoryDTO;
import com.example.actionprice.AuctionData.service.AuctionCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/category") //수정할거
public class AuctionCategoryController {

    private final AuctionCategoryService auctionCategoryService;

    @GetMapping("/{large}/middle") //이런것들도
    public MiddleCategoryDTO getMiddlecategoriesByLarge(@PathVariable String large) {
        return auctionCategoryService.getMiddleCategory(large);
    }

    @GetMapping("/{large}/{middle}/small")
    public SmallCategoryDTO getSmallCategoriesByLargeAndMiddle(
            @PathVariable String large,
            @PathVariable String middle) {
        return auctionCategoryService.getSmallCategory(large, middle);
    }

    @GetMapping("/{large}/{middle}/{small}/product")
    public ProductRankDTO getProductMyLargeMiddleSmall(
            @PathVariable String large,
            @PathVariable String middle,
            @PathVariable String small) {
        return auctionCategoryService.getProductRankCategory(large, middle, small);
    }

    @GetMapping("/{large}/{middle}/{small}/{rank}/price")
    public PriceDTO getPriceMySmallMiddleSmallRank(
            @PathVariable String large,
            @PathVariable String middle,
            @PathVariable String small,
            @PathVariable String rank) {
        return auctionCategoryService.getAveragePrice(large, middle, small, rank);
    }
}
