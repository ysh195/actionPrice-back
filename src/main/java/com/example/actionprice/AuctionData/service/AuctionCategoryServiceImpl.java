package com.example.actionprice.AuctionData.service;

import com.example.actionprice.AuctionData.dto.CategoryResultDTO;
import com.example.actionprice.AuctionData.dto.CategoryDTO;
import com.example.actionprice.AuctionData.entity.*;
import com.example.actionprice.AuctionData.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class AuctionCategoryServiceImpl implements AuctionCategoryService {

    private final AniEntity_repo aniEntity_repo;
    private final FishEntity_repo fishEntity_repo;
    private final FoodCropsEntity_repo foodCropsEntity_repo;
    private final FruitEntity_repo fruitEntity_repo;
    private final SpecialCropsEntity_repo specialCropsEntity_repo;
    private final VegetableEntity_repo vegetableEntity_repo;
    private final CategoryEntity_repo categoryEntity_repo;


    @Override
    public CategoryDTO getMiddleCategory(String large) {
        List<String> list;
        switch (large){
            case "축산물":
                list = categoryEntity_repo.findByLarge(large).stream()
                        .map(AuctionCategoryEntity::getMiddle)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "수산물":
                list = categoryEntity_repo.findByLarge(large).stream()
                        .map(AuctionCategoryEntity::getMiddle)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "식량작물":
                list = categoryEntity_repo.findByLarge(large).stream()
                        .map(AuctionCategoryEntity::getMiddle)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "과일류":
                list = categoryEntity_repo.findByLarge(large).stream()
                        .map(AuctionCategoryEntity::getMiddle)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "특용작물":
                list = categoryEntity_repo.findByLarge(large).stream()
                        .map(AuctionCategoryEntity::getMiddle)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "채소류":
                list = categoryEntity_repo.findByLarge(large).stream()
                        .map(AuctionCategoryEntity::getMiddle)
                        .distinct()
                        .collect(Collectors.toList());
                break;
            default:
                list = List.of();
        }
        return CategoryDTO.builder()
                .large(large)
                .list(list)
                .build();
    }

    @Override
    public CategoryDTO getSmallCategory(String large, String middle) {
        List<String> list;
        switch (large){
            case "축산물":
                list = categoryEntity_repo.findByLargeAndMiddle(large,middle).stream()
                        .map(AuctionCategoryEntity::getProductName)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "수산물":
                list = categoryEntity_repo.findByLargeAndMiddle(large,middle).stream()
                        .map(AuctionCategoryEntity::getProductName)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "식량작물":
                list = categoryEntity_repo.findByLargeAndMiddle(large,middle).stream()
                        .map(AuctionCategoryEntity::getProductName)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "과일류":
                list = categoryEntity_repo.findByLargeAndMiddle(large,middle).stream()
                        .map(AuctionCategoryEntity::getProductName)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "특용작물":
                list = categoryEntity_repo.findByLargeAndMiddle(large,middle).stream()
                        .map(AuctionCategoryEntity::getProductName)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "채소류":
                list = categoryEntity_repo.findByLargeAndMiddle(large,middle).stream()
                        .map(AuctionCategoryEntity::getProductName)
                        .distinct()
                        .collect(Collectors.toList());
                break;
            default:
                list = List.of();
        }
        return CategoryDTO.builder()
                .large(large)
                .middle(middle)
                .list(list)
                .build();
    }

    @Override
    public CategoryDTO getProductRankCategory(String large, String middle, String small) {
        List<String> list;
        switch (large){
            case "축산물":
                list = categoryEntity_repo.findByLargeAndMiddleAndProductName(large,middle,small).stream()
                        .map(AuctionCategoryEntity::getProductRank)
                        .distinct()
                        .collect(Collectors.toList());
                break;

                case "수산물":
                    list = categoryEntity_repo.findByLargeAndMiddleAndProductName(large,middle,small).stream()
                            .map(AuctionCategoryEntity::getProductRank)
                            .distinct()
                            .collect(Collectors.toList());
                    break;
                case "식량작물":
                    list = categoryEntity_repo.findByLargeAndMiddleAndProductName(large,middle,small).stream()
                            .map(AuctionCategoryEntity::getProductRank)
                            .distinct()
                            .collect(Collectors.toList());
                    break;

                case "과일류":
                    list = categoryEntity_repo.findByLargeAndMiddleAndProductName(large,middle,small).stream()
                            .map(AuctionCategoryEntity::getProductRank)
                            .distinct()
                            .collect(Collectors.toList());
                    break;

                case "특용작물":
                    list = categoryEntity_repo.findByLargeAndMiddleAndProductName(large,middle,small).stream()
                            .map(AuctionCategoryEntity::getProductRank)
                                .distinct()
                                .collect(Collectors.toList());
                        break;

                case "채소류":
                    list = categoryEntity_repo.findByLargeAndMiddleAndProductName(large,middle,small).stream()
                            .map(AuctionCategoryEntity::getProductRank)
                                .distinct()
                                .collect(Collectors.toList());
                        break;
                default:
                    list = List.of();
        }
        return CategoryDTO.builder()
                .large(large)
                .middle(middle)
                .small(small)
                .list(list)
                .build();
    }
    
    /**
     * @author homin
     * @created 2024. 11. 1. 오후 4:03
     * @updated 2024. 11. 1. 오후 4:03
     * @info 데이터리스트 및 페이지 작업 타입을 일반 리스트로 다 받는거라 에러날 시  AuctionBaseEntity 자식 객체만 받을 수 있게 설정 가능
     */

    @Override
    public CategoryResultDTO getCategoryAndPage(String large, String middle, String small, String rank, LocalDate startDate, LocalDate endDate, Integer pageNum) {
        LocalDate today = LocalDate.now();
        LocalDate oneYearFromToday = today.plusYears(1);

        // 날짜 유효성 검사 및 설정
        if (startDate == null || startDate.isAfter(oneYearFromToday)) {
            startDate = today;
        }
        if (endDate == null || endDate.isAfter(oneYearFromToday)) {
            endDate = today;
        }

        // 페이징 및 정렬 조건 설정
        Pageable pageable = PageRequest.of(pageNum, 10, Sort.by(Sort.Order.desc("del_id")));
        Page<?> pageResult;

        // 대분류에 따라 적절한 리포지토리 메서드 호출
        switch (large) {
            case "축산물":
                pageResult = aniEntity_repo.findByLargeAndMiddleAndProductNameAndProductRankAndDelDateBetween(
                        large, middle, small, rank, startDate, endDate, pageable);
                break;

            case "수산물":
                pageResult = fishEntity_repo.findByLargeAndMiddleAndProductNameAndProductRankAndDelDateBetween(
                        large, middle, small, rank, startDate, endDate, pageable);
                break;

            case "식량작물":
                pageResult = foodCropsEntity_repo.findByLargeAndMiddleAndProductNameAndProductRankAndDelDateBetween(
                        large, middle, small, rank, startDate, endDate, pageable);
                break;

            case "과일류":
                pageResult = fruitEntity_repo.findByLargeAndMiddleAndProductNameAndProductRankAndDelDateBetween(
                        large, middle, small, rank, startDate, endDate, pageable);
                break;

            case "특용작물":
                pageResult = specialCropsEntity_repo.findByLargeAndMiddleAndProductNameAndProductRankAndDelDateBetween(
                        large, middle, small, rank, startDate, endDate, pageable);
                break;

            case "채소류":
                pageResult = vegetableEntity_repo.findByLargeAndMiddleAndProductNameAndProductRankAndDelDateBetween(
                        large, middle, small, rank, startDate, endDate, pageable);
                break;

            default:
                throw new IllegalArgumentException("Invalid category: " + large);
        }

        return convertPageToDTO(pageResult);
    }

    private <T> CategoryResultDTO convertPageToDTO(Page<T> page) {
        boolean hasContent = (page != null && page.hasContent());

        List<AuctionBaseEntity> transactionHistoryList = hasContent
                ? convertListObject(page.getContent())
                : Collections.emptyList();


        int currentPageNum = hasContent ? (page.getNumber() + 1) : 1;
        int currentPageSize = hasContent ? page.getNumberOfElements() : 0;
        int totalPageNum = hasContent ? page.getTotalPages() : 1;
        boolean hasNext = hasContent && page.hasNext();

        return CategoryResultDTO.builder()
                .transactionHistoryList(transactionHistoryList)
                .currentPageNum(currentPageNum)
                .currentPageSize(currentPageSize)
                .totalPageNum(totalPageNum)
                .listSize(transactionHistoryList.size())
                .hasNext(hasNext)
                .build();
    }

    private <T> List<AuctionBaseEntity> convertListObject(List<T> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream()
                .filter(AuctionBaseEntity.class::isInstance) // AuctionBaseEntity 인스턴스만 필터링
                .map(AuctionBaseEntity.class::cast) // 타입 캐스팅
                .collect(Collectors.toList()); // 리스트로 수집
    }


    /**
     * @author homin
     * @created 2024. 11. 1. 오후 12:43
     * @updated 2024. 11. 1. 오후 12:43
     * @param  transactionHistoryList AuctionBaseEntity 에 담긴 리스트 데이터
     * @info Excel 시트를 위한 로직
     */
    @Override
    public ResponseEntity<byte[]> createExcelFile(List<AuctionBaseEntity> transactionHistoryList) {
        // transactionHistoryList가 null인 경우 처리
        if (transactionHistoryList == null || transactionHistoryList.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("거래 내역이 없습니다.".getBytes());
        }
        // 엑셀 워크북 생성
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("TransactionHistory");

            // 첫 번째 행(인덱스 0)을 생성하여 헤더를 위한 행으로 사용
            Row headerRow = sheet.createRow(0);
            // 헤더 값 설정
            headerRow.createCell(0).setCellValue("날짜");
            headerRow.createCell(1).setCellValue("대분류");
            headerRow.createCell(2).setCellValue("중분류");
            headerRow.createCell(3).setCellValue("소분류");
            headerRow.createCell(4).setCellValue("등급");
            headerRow.createCell(5).setCellValue("단위");
            headerRow.createCell(6).setCellValue("가격");

            // 데이터 추가
            int rowNum = 1;
            for (AuctionBaseEntity entity : transactionHistoryList) {
                // 새로운 행을 생성하고, rowNum을 증가
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(entity.getDelDate().toString());
                row.createCell(1).setCellValue(entity.getLarge());
                row.createCell(2).setCellValue(entity.getMiddle());
                row.createCell(3).setCellValue(entity.getProductName());
                row.createCell(4).setCellValue(entity.getProductRank());
                row.createCell(5).setCellValue(entity.getDel_unit());
                row.createCell(6).setCellValue(entity.getPrice());
            }

            // 내용 기록
            workbook.write(outputStream);

            byte[] excelFile = outputStream.toByteArray(); // 배열 가져오기

            // Content-Disposition 웹 브라우저에게 콘텐츠가 어떻게 처리되어야 하는지를 알려줌
            HttpHeaders headers = new HttpHeaders();
            // attachment 값은 브라우저에게 응답으로 받은 데이터를 파일로 다운로드해야 한다고 지시
            headers.add("Content-Disposition", "attachment; filename=transaction_history.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelFile);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }



}



