package com.example.actionprice.AuctionData.service;

import com.example.actionprice.AuctionData.dto.CategoryItemDTO;
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
import java.util.Map;
import java.util.function.Function;
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
    private final CategoryEntityRepo categoryEntity_repo;

    // 중분류 갖고오기
    @Override
    public CategoryDTO getMiddleCategory(String large) {
        List<CategoryItemDTO> list = getDistinctValues(large, AuctionCategoryEntity::getMiddle, AuctionCategoryEntity::getDelId);
        return CategoryDTO.builder()
                .large(large)
                .list(list)
                .build();
    }

    // 소분류 갖고오기
    @Override
    public CategoryDTO getSmallCategory(String large, String middle) {
        List<CategoryItemDTO> list = getDistinctValues(large, middle, AuctionCategoryEntity::getProductName, AuctionCategoryEntity::getDelId);
        return CategoryDTO.builder()
                .large(large)
                .middle(middle)
                .list(list)
                .build();
    }

    // 등급 갖고오기
    @Override
    public CategoryDTO getProductRankCategory(String large, String middle, String small) {
        List<CategoryItemDTO> list = getDistinctValues(large, middle, small, AuctionCategoryEntity::getProductRank, AuctionCategoryEntity::getDelId);
        return CategoryDTO.builder()
                .large(large)
                .middle(middle)
                .small(small)
                .list(list)
                .build();
    }

    private List<CategoryItemDTO> getDistinctValues(String large, Function<AuctionCategoryEntity, String> mapper, Function<AuctionCategoryEntity, Long> idMapper) {
        return categoryEntity_repo.findByLarge(large).stream()
                .map(entity -> CategoryItemDTO.builder()
                        .name(mapper.apply(entity)) // 카테고리 이름
                        .id(idMapper.apply(entity)) // DB에서 가져온 ID
                        .build())
                .distinct() // ID와 이름 조합으로 중복 제거
                .collect(Collectors.toList());
    }

    private List<CategoryItemDTO> getDistinctValues(String large, String middle, Function<AuctionCategoryEntity, String> mapper, Function<AuctionCategoryEntity, Long> idMapper) {
        return categoryEntity_repo.findByLargeAndMiddle(large, middle).stream()
                .map(entity -> CategoryItemDTO.builder()
                        .name(mapper.apply(entity)) // 카테고리 이름
                        .id(idMapper.apply(entity)) // DB에서 가져온 ID
                        .build())
                .distinct() // ID와 이름 조합으로 중복 제거
                .collect(Collectors.toList());
    }

    private List<CategoryItemDTO> getDistinctValues(String large, String middle, String small, Function<AuctionCategoryEntity, String> mapper, Function<AuctionCategoryEntity, Long> idMapper) {
        return categoryEntity_repo.findByLargeAndMiddleAndProductName(large, middle, small).stream()
                .map(entity -> CategoryItemDTO.builder()
                        .name(mapper.apply(entity)) // 카테고리 이름
                        .id(idMapper.apply(entity)) // DB에서 가져온 ID
                        .build())
                .distinct() // ID와 이름 조합으로 중복 제거
                .collect(Collectors.toList());
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
        startDate = (startDate == null || startDate.isAfter(oneYearFromToday)) ? today : startDate;
        endDate = (endDate == null || endDate.isAfter(oneYearFromToday)) ? today : endDate;

        // startDate가 endDate보다 나중일 경우 예외 처리
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }
        Pageable pageable = PageRequest.of(pageNum, 10, Sort.by(Sort.Order.desc("del_id")));

        // 대분류에 따라 적절한 레포지토리 메서드 호출
        Page<?> pageResult = getPageResultByLarge(large, middle, small, rank, startDate, endDate, pageable);

        return convertPageToDTO(pageResult);
    }

    // 대분류에 따른 리포지토리 메서드 호출을 처리하는 별도의 메서드
    private Page<?> getPageResultByLarge(String large, String middle, String small, String rank, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Map<String, Function<Pageable, Page<?>>> repositoryMap = Map.of(
                "축산물", pageableParam -> aniEntity_repo.findByLargeAndMiddleAndProductNameAndProductRankAndDelDateBetween(large, middle, small, rank, startDate, endDate, pageableParam),
                "수산물", pageableParam -> fishEntity_repo.findByLargeAndMiddleAndProductNameAndProductRankAndDelDateBetween(large, middle, small, rank, startDate, endDate, pageableParam),
                "식량작물", pageableParam -> foodCropsEntity_repo.findByLargeAndMiddleAndProductNameAndProductRankAndDelDateBetween(large, middle, small, rank, startDate, endDate, pageableParam),
                "과일류", pageableParam -> fruitEntity_repo.findByLargeAndMiddleAndProductNameAndProductRankAndDelDateBetween(large, middle, small, rank, startDate, endDate, pageableParam),
                "특용작물", pageableParam -> specialCropsEntity_repo.findByLargeAndMiddleAndProductNameAndProductRankAndDelDateBetween(large, middle, small, rank, startDate, endDate, pageableParam),
                "채소류", pageableParam -> vegetableEntity_repo.findByLargeAndMiddleAndProductNameAndProductRankAndDelDateBetween(large, middle, small, rank, startDate, endDate, pageableParam)
        );

        Function<Pageable, Page<?>> repositoryMethod = repositoryMap.get(large);
        // 대분류가 유효하지 않으면 예외 발생
        if (repositoryMethod == null) {
            throw new IllegalArgumentException("Invalid category: " + large);
        }

        return repositoryMethod.apply(pageable);
    }

    private <T> CategoryResultDTO convertPageToDTO(Page<T> page) {
        boolean hasContent = (page != null && page.hasContent());

        // 거래 내역 리스트 생성 (내용이 있으면 변환, 없으면 빈 리스트)
        List<AuctionBaseEntity> transactionHistoryList = hasContent
                ? convertListObject(page.getContent())
                : Collections.emptyList();

        // ID 리스트 생성
        List<Long> transactionIds = transactionHistoryList.stream()
                .map(AuctionBaseEntity::getDelId) // 각 거래 내역의 ID를 가져옴
                .collect(Collectors.toList());

        int currentPageNum = hasContent ? (page.getNumber() + 1) : 1; // 1 기반 페이지 번호
        int currentPageSize = hasContent ? page.getNumberOfElements() : 0; // 현재 페이지의 요소 수
        int totalPageNum = hasContent ? page.getTotalPages() : 1; // 총 페이지 수
        boolean hasNext = hasContent && page.hasNext(); // 다음 페이지 여부

        return CategoryResultDTO.builder()
                .transactionHistoryList(transactionHistoryList)
                .currentPageNum(currentPageNum)
                .currentPageSize(currentPageSize)
                .totalPageNum(totalPageNum)
                .listSize(transactionHistoryList.size())
                .hasNext(hasNext)
                .transactionIds(transactionIds) // ID 리스트 추가
                .build();
    }

    // 리스트의 요소를 AuctionBaseEntity 타입으로 변환하는 메서드
    private <T> List<AuctionBaseEntity> convertListObject(List<T> list) {
        return list.stream()
                .filter(AuctionBaseEntity.class::isInstance) // AuctionBaseEntity 타입 필터링
                .map(AuctionBaseEntity.class::cast) // 필터링된 객체를 AuctionBaseEntity로 변환
                .collect(Collectors.toList());
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



