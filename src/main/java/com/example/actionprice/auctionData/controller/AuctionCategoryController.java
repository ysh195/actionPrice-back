package com.example.actionprice.auctionData.controller;

import com.example.actionprice.auctionData.dto.CategoryResultDTO;
import com.example.actionprice.auctionData.dto.CategoryDTO;
import com.example.actionprice.auctionData.dto.ChartDataDTO;
import com.example.actionprice.auctionData.entity.AuctionBaseEntity;
import com.example.actionprice.auctionData.service.AuctionCategoryService;
import com.example.actionprice.auctionData.service.AuctionEntityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 도매시장 거래내역 조회에 필요한, 카테고리 검색에 사용되는 컨트롤러
 * @info 우리가 확보한 데이터가 현재로부터 1년 전이기 때문에 1년을 초과하는 기간은 조회할 수 없음
 * @info 그렇기 때문에 데이터 조회 시 1년을 초과하는 조회가 불가능하도록 설정
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/category")
@Log4j2
public class AuctionCategoryController {

    private final AuctionCategoryService auctionCategoryService;
    private final AuctionEntityService auctionEntityService;

    /**
     * 중분류 카테고리 조회
     * @param large 대분류 카테고리
     */
    @GetMapping("/{large}")
    public CategoryDTO getMiddleCategoriesByLarge(@PathVariable String large) {
        log.info("[class] AuctionCategoryController - [method] getCategoriesByLarge - large : {}", large);
        return auctionCategoryService.getMiddleCategory(large);
    }

    /**
     * 소분류 카테고리 조회
     * @param large 대분류 카테고리
     * @param middle 중분류 카테고리
     */
    @GetMapping("/{large}/{middle}")
    public CategoryDTO getSmallCategoriesByLargeAndMiddle(
            @PathVariable String large,
            @PathVariable String middle) {
        log.info("[class] AuctionCategoryController - [method] getCategoriesByLargeAndMiddle - large : {} | middle : {}", large, middle);
        return auctionCategoryService.getSmallCategory(large, middle);
    }

    /**
     * 상품등급 카테고리 조회
     * @param large 대분류 카테고리
     * @param middle 중분류 카테고리
     * @param small 소분류 카테고리
     */
    @GetMapping("/{large}/{middle}/{small}")
    public CategoryDTO getRankCategoriesByLargeMiddleSmall(
            @PathVariable String large,
            @PathVariable String middle,
            @PathVariable String small) {
        log.info("[class] AuctionCategoryController - [method] getCategoriesMyLargeMiddleSmall - large : {} | middle : {} | small : {}", large, middle, small);
        return auctionCategoryService.getProductRankCategory(large, middle, small);
    }

    /**
     * 거래내역 조회(리스트)
     * @param large 대분류 카테고리
     * @param middle 중분류 카테고리
     * @param small 소분류 카테고리
     * @param rank 상품등급 카테고리
     * @param startDate 조회할 날짜(시작)
     * @param endDate 조회할 날짜(종료)
     * @param pageNum 페이지 번호(pagenation)
     */
    @GetMapping("/{large}/{middle}/{small}/{rank}")
    public CategoryResultDTO getPriceBySmallMiddleSmallRank(
            @PathVariable String large,
            @PathVariable String middle,
            @PathVariable String small,
            @PathVariable String rank,
            @RequestParam(value = "startDate",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(name = "pageNum", defaultValue = "0", required = false) Integer pageNum
    ) {

        LocalDate today = LocalDate.now();
        LocalDate oneYearAgo = today.minusYears(1);

        boolean isEndDateIncorrect = endDate == null || endDate.isBefore(oneYearAgo);
        if (isEndDateIncorrect) {
            endDate = today; // 기본값으로 오늘 날짜로 설정
        }

        boolean isStartDateIncorrect =
            startDate == null  || startDate.isBefore(oneYearAgo) || startDate.isAfter(endDate);
        if (isStartDateIncorrect) {
            // 기본값으로 오늘 날짜로 설정
            startDate = today;
        }

        return auctionEntityService.getCategoryAndPage(
            large,
            middle,
            small,
            rank,
            startDate,
            endDate,
            pageNum
        );
    }

    /**
     * 거래내역 조회(그래프)
     * @param large 대분류 카테고리
     * @param middle 중분류 카테고리
     * @param small 소분류 카테고리
     * @param rank 상품등급 카테고리
     * @param startDate 조회할 날짜(시작)
     * @param endDate 조회할 날짜(종료)
     */
    @GetMapping("/{large}/{middle}/{small}/{rank}/gragh")
    public ChartDataDTO getPriceDataWithGragh(
        @PathVariable String large,
        @PathVariable String middle,
        @PathVariable String small,
        @PathVariable String rank,
        @RequestParam(value = "startDate",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @RequestParam(value = "endDate",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        log.info("그래프 출력 시작");
        LocalDate today = LocalDate.now();
        LocalDate oneYearAgo = today.minusYears(1);

        boolean isEndDateIncorrect = endDate == null || endDate.isBefore(oneYearAgo);
        if (isEndDateIncorrect) {
            endDate = today; // 기본값으로 오늘 날짜로 설정
        }

        boolean isStartDateIncorrect =
            startDate == null  || startDate.isBefore(oneYearAgo) || startDate.isAfter(endDate);
        if (isStartDateIncorrect) {
            // 기본값으로 오늘 날짜로 설정
            startDate = today;
        }

        log.info("그래프 출력 완료");
        return auctionEntityService.getChartData(
            large,
            middle,
            small,
            rank,
            startDate,
            endDate
        );
    }

    /**
     * 거래내역 엑셀 파일로 다운로드
     * @param large 대분류 카테고리
     * @param middle 중분류 카테고리
     * @param small 소분류 카테고리
     * @param rank 상품등급 카테고리
     * @param startDate 조회할 날짜(시작)
     * @param endDate 조회할 날짜(종료)
     */
    @GetMapping("/{large}/{middle}/{small}/{rank}/excel")
    public ResponseEntity<byte[]> downloadExcel(
            @PathVariable String large,
            @PathVariable String middle,
            @PathVariable String small,
            @PathVariable String rank,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        LocalDate today = LocalDate.now();
        LocalDate oneYearAgo = today.minusYears(1);

        boolean isEndDateIncorrect = endDate == null || endDate.isBefore(oneYearAgo);
        if (isEndDateIncorrect) {
            endDate = today; // 기본값으로 오늘 날짜로 설정
        }

        boolean isStartDateIncorrect =
            startDate == null  || startDate.isBefore(oneYearAgo) || startDate.isAfter(endDate);
        if (isStartDateIncorrect) {
            // 기본값으로 오늘 날짜로 설정
            startDate = today;
        }

        // 페이지 없이 데이터를 가져오는 서비스 메서드 호출
        List<AuctionBaseEntity> transactionHistoryList =
            auctionEntityService.fetchTransactionHistoryList(
                large,
                middle,
                small,
                rank,
                startDate,
                endDate
            );

        // 엑셀 파일 생성
        byte[] excelFile = auctionEntityService.createExcelFile(transactionHistoryList);

        // Content-Disposition 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add(
            "Content-Disposition",
            "attachment; filename=transaction_history.xlsx"
        );

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelFile);
    }
}
