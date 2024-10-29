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
@RequestMapping("/api/category") // 니중에 고칠거
public class AuctionCategoryController {

    private final AuctionCategoryService auctionCategoryService;


    @GetMapping("{large}/middle") //마찬가지
    public MiddleCategoryDTO getMiddlecategoriesByLarge(@RequestParam String large) {
        return auctionCategoryService.getMiddleCategory(large);
    }
//    @GetMapping("/{large}/middle")
//    public MiddleCategoryDTO getMiddleCategories(@PathVariable String large) {
//        return auctionCategoryService.getMiddleCategory(large);
//    }

    @GetMapping("{large}/middle/small")
    public SmallCategoryDTO getSmallCategoriesByLargeAndMiddle(
            @RequestParam String large,
            @RequestParam String middle) {
        return auctionCategoryService.getSmallCategory(large, middle);
    }
//    @GetMapping("/{large}/{middle}/small")
//    public SmallCategoryDTO getSmallCategories(@PathVariable String large, @PathVariable String middle) {
//        return auctionCategoryService.getSmallCategory(large, middle);
//    }

    @GetMapping("/middle/small/product")
    public ProductRankDTO getProductMyLargeMiddleSmall(
            @RequestParam String large,
            @RequestParam String middle,
            @RequestParam String small) {
        return auctionCategoryService.getProductRankCategory(large,middle,small);
    }
//        ProductRankDTO productRankDTO = auctionCategoryService.getProductRankCategory(large, middle, small);
//        return ResponseEntity.ok(productRankDTO);

//    @GetMapping("/{large}/{middle}/{small}/product")
//    public ProductRankDTO getProductRank(@PathVariable String large, @PathVariable String middle, @PathVariable String small) {
//        return auctionCategoryService.getProductRankCategory(large, middle, small);
//    }

    @GetMapping("/middle/small/product/price")
    public PriceDTO getPriceMySmallMiddleSmallRank(
            @RequestParam String large,
            @RequestParam String middle,
            @RequestParam String small,
            @RequestParam String rank) {
        return auctionCategoryService.getAveragePrice(large,middle,small,rank);

    }
}


