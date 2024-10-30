package com.example.actionprice.AuctionData.controller;

import com.example.actionprice.AuctionData.dto.MiddleCategoryDTO;
import com.example.actionprice.AuctionData.dto.PriceDTO;
import com.example.actionprice.AuctionData.dto.CategoryDTO;
import com.example.actionprice.AuctionData.dto.SmallCategoryDTO;
import com.example.actionprice.AuctionData.service.AuctionCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/category") //수정할거
public class AuctionCategoryController {

    private final AuctionCategoryService auctionCategoryService;

    @GetMapping("/{large}/middle") //이런것들도
    public CategoryDTO getCaterotysByLarge(@PathVariable String large) {
        return auctionCategoryService.getMiddleCategory(large);
    }

    @GetMapping("/{large}/{middle}/small")
    public CategoryDTO getCaterotysByLargeAndMiddle(
            @PathVariable String large,
            @PathVariable String middle) {
        return auctionCategoryService.getSmallCategory(large, middle);
    }

    @GetMapping("/{large}/{middle}/{small}/product")
    public CategoryDTO getCaterotysMyLargeMiddleSmall(
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
            @PathVariable String rank,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return auctionCategoryService.getAveragePrice(large, middle, small, rank,startDate, endDate);
    }
}
