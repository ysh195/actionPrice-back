package com.example.actionprice.AuctionData.service;

import com.example.actionprice.AuctionData.dto.MiddleCategoryDTO;
import com.example.actionprice.AuctionData.dto.PriceDTO;
import com.example.actionprice.AuctionData.dto.ProductRankDTO;
import com.example.actionprice.AuctionData.dto.SmallCategoryDTO;
import com.example.actionprice.AuctionData.entity.*;
import com.example.actionprice.AuctionData.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


    @Override
    public MiddleCategoryDTO getMiddleCategory(String large) {
        List<String> middleCategories;
        switch (large){
            case "축산물":
                middleCategories = aniEntity_repo.findByLarge(large).stream()
                        .map(AuctionEntity_ani::getMiddle)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "수산물":
                middleCategories = fishEntity_repo.findByMiddle(large).stream()
                        .map(AuctionEntity_fish::getMiddle)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "식량작물":
                middleCategories = foodCropsEntity_repo.findByMiddle(large).stream()
                        .map(AuctionEntity_foodCrops::getMiddle)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "과일류":
                middleCategories = fruitEntity_repo.findByMiddle(large).stream()
                        .map(AuctionEntity_fruit::getMiddle)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "특용작물":
                middleCategories = specialCropsEntity_repo.findByMiddle(large).stream()
                        .map(AuctionEntity_specialCrop::getMiddle)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "채소류":
                middleCategories = vegetableEntity_repo.findByMiddle(large).stream()
                        .map(AuctionEntity_vegetable::getMiddle)
                        .distinct()
                        .collect(Collectors.toList());
                break;
            default:
                middleCategories = List.of();
        }
        return MiddleCategoryDTO.builder()
                .large(large)
                .middleCategories(middleCategories)
                .build();
    }

    @Override
    public SmallCategoryDTO getSmallCategory(String large, String middle) {
        List<String> smallCategories;
        switch (large){
            case "축산물":
                smallCategories = aniEntity_repo.findByLargeAndMiddle(large,middle).stream()
                        .map(AuctionEntity_ani::getProduct_name)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "수산물":
                smallCategories = fishEntity_repo.findBySmall(large,middle).stream()
                        .map(AuctionEntity_fish::getProduct_name)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "식량작물":
                smallCategories = foodCropsEntity_repo.findBySmall(large,middle).stream()
                        .map(AuctionEntity_foodCrops::getProduct_name)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "과일류":
                smallCategories = fruitEntity_repo.findBySmall(large,middle).stream()
                        .map(AuctionEntity_fruit::getProduct_name)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "특용작물":
                smallCategories = specialCropsEntity_repo.findBySmall(large,middle).stream()
                        .map(AuctionEntity_specialCrop::getProduct_name)
                        .distinct()
                        .collect(Collectors.toList());
                break;

            case "채소류":
                smallCategories = vegetableEntity_repo.findBySmall(large,middle).stream()
                        .map(AuctionEntity_vegetable::getProduct_name)
                        .distinct()
                        .collect(Collectors.toList());
                break;
            default:
                smallCategories = List.of();
        }
        return SmallCategoryDTO.builder()
                .large(large)
                .middle(middle)
                .smallCategories(smallCategories)
                .build();
    }

    @Override
    public ProductRankDTO getProductRankCategory(String large, String middle, String small) {
        List<String> productRankCategories;
        switch (large){
            case "축산물":
                productRankCategories = aniEntity_repo.findByLargeAndMiddleAndProduct_name(large,middle,small).stream()
                        .map(AuctionEntity_ani::getProduct_rank)
                        .distinct()
                        .collect(Collectors.toList());
                break;

                case "수산물":
                    productRankCategories = fishEntity_repo.findbyProductRank(large,middle,small).stream()
                            .map(AuctionEntity_fish::getProduct_rank)
                            .distinct()
                            .collect(Collectors.toList());
                    break;
                case "식량작물":
                    productRankCategories = foodCropsEntity_repo.findbyProductRank(large,middle,small).stream()
                            .map(AuctionEntity_foodCrops::getProduct_rank)
                            .distinct()
                            .collect(Collectors.toList());
                    break;

                case "과일류":
                    productRankCategories = fruitEntity_repo.findbyProductRank(large,middle,small).stream()
                            .map(AuctionEntity_fruit::getProduct_rank)
                            .distinct()
                            .collect(Collectors.toList());
                    break;

                case "특용작물":
                        productRankCategories = specialCropsEntity_repo.findbyProductRank(large,middle,small).stream()
                                .map(AuctionEntity_specialCrop::getProduct_rank)
                                .distinct()
                                .collect(Collectors.toList());
                        break;

                case "채소류":
                        productRankCategories = vegetableEntity_repo.findbyProductRank(large,middle,small).stream()
                                .map(AuctionEntity_vegetable::getProduct_rank)
                                .distinct()
                                .collect(Collectors.toList());
                        break;
                default:
                        productRankCategories = List.of();
        }
        return ProductRankDTO.builder()
                .large(large)
                .middle(middle)
                .small(small)
                .productRankCategories(productRankCategories)
                .build();
    }

    @Override
    public PriceDTO getAveragePrice(String large, String middle, String small, String rank) {
        List<Integer> prices;
        switch (large) {
            case "축산물":
                prices = aniEntity_repo.findByLargeAndMiddleAndProduct_nameAndProduct_rank(large, middle, small, rank).stream()
                        .map(AuctionEntity_ani::getPrice)
                        .collect(Collectors.toList());
                break;
            case "수산물":
                prices = fishEntity_repo.findByPrice(large, middle, small, rank).stream()
                        .map(AuctionEntity_fish::getPrice)
                        .collect(Collectors.toList());
                break;
            case "식량작물":
                prices = foodCropsEntity_repo.findByPrice(large, middle, small, rank).stream()
                        .map(AuctionEntity_foodCrops::getPrice)
                        .collect(Collectors.toList());
                break;
            case "과일류":
                prices = fruitEntity_repo.findByPrice(large, middle, small, rank).stream()
                        .map(AuctionEntity_fruit::getPrice)
                        .collect(Collectors.toList());
                break;
            case "특용작물":
                prices = specialCropsEntity_repo.findByPrice(large, middle, small, rank).stream()
                        .map(AuctionEntity_specialCrop::getPrice)
                        .collect(Collectors.toList());
                break;
            case "채소류":
                prices = vegetableEntity_repo.findByPrice(large, middle, small, rank).stream()
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
                .averagePrice(averagePrice)
                .build();
    }
}



