package com.example.actionprice.AuctionData.controller;

import com.example.actionprice.AuctionData.dto.AuctionCategoryDTO;
import com.example.actionprice.AuctionData.service.AuctionCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auctions") // 니중에 고칠거
public class AuctionCategoryController {

    private final AuctionCategoryService auctionCategoryService;

    public AuctionCategoryController(AuctionCategoryService auctionCategoryService) {
        this.auctionCategoryService = auctionCategoryService;
    }

    @GetMapping("/categories") //마찬가지
    public ResponseEntity<AuctionCategoryDTO> getCategoriesByLarge(@RequestParam String large) {
        AuctionCategoryDTO result = auctionCategoryService.categorizeAuctionsByLarge(large);
        return ResponseEntity.ok(result); // DTO 반환
    }
}
