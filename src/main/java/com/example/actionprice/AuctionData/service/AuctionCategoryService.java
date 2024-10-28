package com.example.actionprice.AuctionData.service;

import com.example.actionprice.AuctionData.dto.AuctionCategoryDTO;
import com.example.actionprice.AuctionData.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuctionCategoryService {
    private final AniEntity_repo aniEntity_repo;
    private final FishEntity_repo fishEntity_repo;
    private final FoodCropsEntity_repo foodCropsEntity_repo;
    private final FruitEntity_repo fruitEntity_repo;
    private final SpecialCropsEntity_repo specialCropsEntity_repo;
    private final VegetableEntity_repo vegetableEntity_repo;

    public AuctionCategoryService(AniEntity_repo aniEntity_repo, FishEntity_repo fishEntity_repo,
                                  FoodCropsEntity_repo foodCropsEntity_repo, FruitEntity_repo fruitEntity_repo,
                                  SpecialCropsEntity_repo specialCropsEntity_repo,
                                  VegetableEntity_repo vegetableEntity_repo) {
        this.aniEntity_repo = aniEntity_repo;
        this.fishEntity_repo = fishEntity_repo;
        this.foodCropsEntity_repo = foodCropsEntity_repo;
        this.fruitEntity_repo = fruitEntity_repo;
        this.specialCropsEntity_repo = specialCropsEntity_repo;
        this.vegetableEntity_repo = vegetableEntity_repo;
    }

    public AuctionCategoryDTO categorizeAuctionsByLarge(String large) {
        Map<String, List<String>> middleCategories = new HashMap<>();
        List<Integer> prices = new ArrayList<>();

        //근데 프론트에서 어떻게 large값을 요청할지 모름
        // large 값에 따라 리포지토리에서 데이터 조회
        switch (large) {
            case "축산물":
                aniEntity_repo.findByLarge(large).forEach(entity -> {
                    middleCategories
                            .computeIfAbsent(entity.getMiddle(), k -> new ArrayList<>())
                            .add(entity.getProduct_name());
                    prices.add(entity.getPrice());
                });
                break;

            case "수산물":
                fishEntity_repo.findByLarge(large).forEach(entity -> {
                    middleCategories
                            .computeIfAbsent(entity.getMiddle(), k -> new ArrayList<>())
                            .add(entity.getProduct_name());
                    prices.add(entity.getPrice());
                });
                break;

            case "식량작물":
                foodCropsEntity_repo.findByLarge(large).forEach(entity -> {
                    middleCategories
                            .computeIfAbsent(entity.getMiddle(), k -> new ArrayList<>())
                            .add(entity.getProduct_name());
                    prices.add(entity.getPrice());
                });
                break;

            case "과일류":
                fruitEntity_repo.findByLarge(large).forEach(entity -> {
                    middleCategories
                            .computeIfAbsent(entity.getMiddle(), k -> new ArrayList<>())
                            .add(entity.getProduct_name());
                    prices.add(entity.getPrice());
                });
                break;

            case "특용작물":
                specialCropsEntity_repo.findByLarge(large).forEach(entity -> {
                    middleCategories
                            .computeIfAbsent(entity.getMiddle(), k -> new ArrayList<>())
                            .add(entity.getProduct_name());
                    prices.add(entity.getPrice());
                });
                break;

            case "채소류":
                vegetableEntity_repo.findByLarge(large).forEach(entity -> {
                    middleCategories
                            .computeIfAbsent(entity.getMiddle(), k -> new ArrayList<>())
                            .add(entity.getProduct_name());
                    prices.add(entity.getPrice());
                });
                break;

            default:
                throw new IllegalArgumentException("Invalid category: " + large);
        }

        // 가격 평균 계산
        double averagePrice = prices.stream().mapToInt(Integer::intValue).average().orElse(0);

        return new AuctionCategoryDTO(middleCategories, averagePrice); // DTO 반환
    }
}
