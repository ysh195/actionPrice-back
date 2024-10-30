package com.example.actionprice.AuctionData.service;

import com.example.actionprice.AuctionData.dto.PriceDTO;
import com.example.actionprice.AuctionData.dto.CategoryDTO;
import com.example.actionprice.AuctionData.entity.*;
import com.example.actionprice.AuctionData.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
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

    @Override
    public PriceDTO getAveragePrice(String large, String middle, String small, String rank, LocalDate startDate, LocalDate endDate) {
        List<Integer> prices;
        switch (large) {
            case "축산물":
                prices = aniEntity_repo.findByLargeAndMiddleAndProductNameAndProductRankAndDelDateBetween(large, middle, small, rank, startDate, endDate).stream()
                        .map(AuctionEntity_ani::getPrice)
                        .collect(Collectors.toList());
                break;
            case "수산물":
                prices = fishEntity_repo.findByLargeAndMiddleAndProductNameAndProductRank(large, middle, small, rank).stream()
                        .map(AuctionEntity_fish::getPrice)
                        .collect(Collectors.toList());
                break;
            case "식량작물":
                prices = foodCropsEntity_repo.findByLargeAndMiddleAndProductNameAndProductRank(large, middle, small, rank).stream()
                        .map(AuctionEntity_foodCrops::getPrice)
                        .collect(Collectors.toList());
                break;
            case "과일류":
                prices = fruitEntity_repo.findByLargeAndMiddleAndProductNameAndProductRank(large, middle, small, rank).stream()
                        .map(AuctionEntity_fruit::getPrice)
                        .collect(Collectors.toList());
                break;
            case "특용작물":
                prices = specialCropsEntity_repo.findByLargeAndMiddleAndProductNameAndProductRank(large, middle, small, rank).stream()
                        .map(AuctionEntity_specialCrop::getPrice)
                        .collect(Collectors.toList());
                break;
            case "채소류":
                prices = vegetableEntity_repo.findByLargeAndMiddleAndProductNameAndProductRank(large, middle, small, rank).stream()
                        .map(AuctionEntity_vegetable::getPrice)
                        .collect(Collectors.toList());
                break;
            default:
                prices = List.of();
        }
        OptionalDouble average = prices.stream().mapToInt(Integer::intValue).average();
        int averagePrice = (int) Math.round(average.orElse(0.0));
        return PriceDTO.builder()
                .large(large)
                .middle(middle)
                .small(small)
                .rank(rank)
                .startDate(startDate)
                .endDate(endDate)
                .averagePrice(averagePrice)
                .build();
    }
}



