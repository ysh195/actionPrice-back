package com.example.actionprice.AuctionData.controller;

import com.example.actionprice.AuctionData.dto.CategorizedAuctionDTO;
import com.example.actionprice.AuctionData.service.AuctionCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("category")
public class AuctionCategoryController {

    private final AuctionCategoryService auctionCategoryService;

    public AuctionCategoryController(AuctionCategoryService auctionCategoryService) {
        this.auctionCategoryService = auctionCategoryService;
    }

    @GetMapping("/categorize")
    public ResponseEntity<CategorizedAuctionDTO> getCategorizedAuctions() {
        CategorizedAuctionDTO categorizedAuctions = auctionCategoryService.categorizeAuctions();
        return ResponseEntity.ok(categorizedAuctions);
    }
}
