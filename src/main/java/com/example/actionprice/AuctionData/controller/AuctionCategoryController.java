package com.example.actionprice.AuctionData.controller;

import com.example.actionprice.AuctionData.dto.CategoryResultDTO;
import com.example.actionprice.AuctionData.dto.CategoryDTO;
import com.example.actionprice.AuctionData.entity.AuctionBaseEntity;
import com.example.actionprice.AuctionData.service.AuctionCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/category") //수정할거
public class AuctionCategoryController {

    private final AuctionCategoryService auctionCategoryService;

    @GetMapping("/{large}") //이런것들도
    public CategoryDTO getCategoriesByLarge(@PathVariable String large) {
        return auctionCategoryService.getMiddleCategory(large);
    }

    @GetMapping("/{large}/{middle}")
    public CategoryDTO getCategoriesByLargeAndMiddle(
            @PathVariable String large,
            @PathVariable String middle) {
        return auctionCategoryService.getSmallCategory(large, middle);
    }

    @GetMapping("/{large}/{middle}/{small}")
    public CategoryDTO getCategoriesMyLargeMiddleSmall(
            @PathVariable String large,
            @PathVariable String middle,
            @PathVariable String small) {
        return auctionCategoryService.getProductRankCategory(large, middle, small);
    }

    @GetMapping("/{large}/{middle}/{small}/{rank}")
    public CategoryResultDTO getPriceMySmallMiddleSmallRank(
            @PathVariable String large,
            @PathVariable String middle,
            @PathVariable String small,
            @PathVariable String rank,
            @RequestParam(value = "startDate",required = false,defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate",required = false,defaultValue = "") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(name = "pageNum", defaultValue = "0", required = false) Integer pageNum
    ) {
        return auctionCategoryService.getCategoryAndPage(large, middle, small, rank,startDate, endDate, pageNum);
    }


    @GetMapping("/{large}/{middle}/{small}/{rank}/excel")
    public ResponseEntity<byte[]> downloadExcel(
            @PathVariable String large,
            @PathVariable String middle,
            @PathVariable String small,
            @PathVariable String rank,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(name = "pageNum", defaultValue = "0", required = false) Integer pageNum) {

        CategoryResultDTO resultDTO = auctionCategoryService.getCategoryAndPage(large, middle, small, rank, startDate, endDate, pageNum);
        List<AuctionBaseEntity> transactionHistoryList = resultDTO.getTransactionHistoryList();

        return auctionCategoryService.createExcelFile(transactionHistoryList);
    }
}
