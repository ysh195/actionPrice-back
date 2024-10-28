package com.example.actionprice.AuctionData.service;

import com.example.actionprice.AuctionData.dto.CategorizedAuctionDTO;
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

    public AuctionCategoryService(AniEntity_repo aniEntity_repo, FishEntity_repo fishEntity_repo, FoodCropsEntity_repo foodCropsEntity_repo, FruitEntity_repo fruitEntity_repo, SpecialCropsEntity_repo specialCropsEntity_repo, VegetableEntity_repo vegetableEntity_repo) {
        this.aniEntity_repo = aniEntity_repo;
        this.fishEntity_repo = fishEntity_repo;
        this.foodCropsEntity_repo = foodCropsEntity_repo;
        this.fruitEntity_repo = fruitEntity_repo;
        this.specialCropsEntity_repo = specialCropsEntity_repo;
        this.vegetableEntity_repo = vegetableEntity_repo;
    }

    public CategorizedAuctionDTO categorizeAuctions() {
        Map<String, Map<String, List<String>>> categorizedAuctions = new HashMap<>();

        aniEntity_repo.findAll().forEach(entity -> {
            categorizedAuctions
                    .computeIfAbsent(entity.getLarge(), k -> new HashMap<>())
                    .computeIfAbsent(entity.getMiddle(), k -> new ArrayList<>())
                    .add(entity.getProduct_name());
        });

        // 나머지 테이블에서도 동일하게 처리
        fishEntity_repo.findAll().forEach(entity -> {
            categorizedAuctions
                    .computeIfAbsent(entity.getLarge(), k -> new HashMap<>())
                    .computeIfAbsent(entity.getMiddle(), k -> new ArrayList<>())
                    .add(entity.getProduct_name());
        });

        foodCropsEntity_repo.findAll().forEach(entity -> {
            categorizedAuctions
                    .computeIfAbsent(entity.getLarge(), k -> new HashMap<>())
                    .computeIfAbsent(entity.getMiddle(), k -> new ArrayList<>())
                    .add(entity.getProduct_name());
        });

        fruitEntity_repo.findAll().forEach(entity -> {
            categorizedAuctions
                    .computeIfAbsent(entity.getLarge(), k -> new HashMap<>())
                    .computeIfAbsent(entity.getMiddle(), k -> new ArrayList<>())
                    .add(entity.getProduct_name());
        });

        specialCropsEntity_repo.findAll().forEach(entity -> {
            categorizedAuctions
                    .computeIfAbsent(entity.getLarge(), k -> new HashMap<>())
                    .computeIfAbsent(entity.getMiddle(), k -> new ArrayList<>())
                    .add(entity.getProduct_name());
        });

        vegetableEntity_repo.findAll().forEach(entity -> {
            categorizedAuctions
                    .computeIfAbsent(entity.getLarge(), k -> new HashMap<>())
                    .computeIfAbsent(entity.getMiddle(), k -> new ArrayList<>())
                    .add(entity.getProduct_name());
        });

        return new CategorizedAuctionDTO(categorizedAuctions); // DTO 객체로 변환하여 반환
    }
}
