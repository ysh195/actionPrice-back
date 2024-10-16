package com.example.actionprice.auctionData;

import com.example.actionprice.AuctionData.detailCategory.AllSortingComponent;
import com.example.actionprice.AuctionData.detailCategory.DetailCategoryEntity;
import com.example.actionprice.AuctionData.detailCategory.DetailCategoryRepository;
import com.example.actionprice.AuctionData.entity.AuctionEntity_crops;
import com.example.actionprice.AuctionData.repository.AniEntity_repo;
import com.example.actionprice.AuctionData.repository.CropsEntity_repo;
import com.example.actionprice.AuctionData.repository.FishEntity_repo;
import com.example.actionprice.newAuctionData.NewAuctionDataFetcher;
import com.example.actionprice.newAuctionData.newApiRequestObj.NewAuctionDataRow;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
@Log4j2
public class newTest {

    @Autowired
    NewAuctionDataFetcher newAuctionDataFetcher;

    @Autowired
    AllSortingComponent allSortingComponent;

    @Autowired
    private DetailCategoryRepository detailCategoryRepository;

    @Autowired
    AniEntity_repo aniEntity_repo;

    @Autowired
    CropsEntity_repo cropsEntity_repo;

    @Autowired
    FishEntity_repo fishEntity_repo;


    @Test
    void newAuctionDataTest() throws Exception {

        String year = "2023";
        String month = "08";

        int endDay = 1; // 각 달의 마지막 날이 언제인지 확인 후 입력

        for (int i = 1; i <= endDay; i++) {
            String day = String.valueOf(i);
            if (day.length() == 1) {
                day = "0" + day;
            }


            String date = String.format("%s%s%s", year, month, day);

            Flux<NewAuctionDataRow> auctionDataFlux = newAuctionDataFetcher.getNewAuctionData_Flux(date);

            auctionDataFlux.toStream().map(row -> {
                try{
                    String str = String.format("%s%s%s",row.getLarge(),row.getMid(),row.getSmall());

                    DetailCategoryEntity detailCategory = detailCategoryRepository.findById(str).orElse(null);
                    if(detailCategory != null){
                        String grandSort = allSortingComponent.getGrand_category_map().get(detailCategory.getLarge());

                        if (grandSort != null) { // Null 체크
                            switch (grandSort) {
                                case "농산물":
                                    return convertRowToCropsEntity(row, detailCategory);
                                case "수산물":
                                    return convertRowToFishEntity(row, detailCategory);
                                case "축산물":
                                    return convertRowToAniEntity(row, detailCategory);
                                default:
                                    System.out.println(grandSort + "는 존재하지 않는 항목입니다.");
                                    break;
                            }
                        } else {
                            System.out.println("grandSort가 null입니다.");
                        }
                    } else {
                        System.out.println("Category not found");
                    }
                } catch (Exception e) {
                    log.error(e);
                    return null;
                }
                return null;
            }).forEach(entity -> {
                if(entity != null){
                    System.out.println(entity);
                    String grandSort = allSortingComponent.getGrand_category_map().get(entity.getLarge());
                    System.out.println("농/수/축 분류 : " + grandSort);
                    switch (grandSort){
                        case "농산물":
                            System.out.println(entity.toString());
                            // cropsEntity_repo.save((AuctionEntity_crops)entity);
                            break;
                        case "수산물":
                            System.out.println(entity.toString());
                            // fishEntity_repo.save((AuctionEntity_fish) entity);
                            break;
                        case "축산물":
                            System.out.println(entity.toString());
                            // aniEntity_repo.save((AuctionEntity_ani) entity);
                            break;
                        default:
                            break;
                    }

                }
            });
        }



    }
    private AuctionEntity_crops convertRowToCropsEntity(NewAuctionDataRow row, DetailCategoryEntity detailCategory) {
        return AuctionEntity_crops.builder()
                .del_date(allSortingComponent.convertStrToLocalDate(row.getSaledate()))
                .large(detailCategory.getLarge())
                .middle(detailCategory.getMiddle())
                .small(detailCategory.getSmall())
                .product_name(detailCategory.getSmall())
                .market_name(allSortingComponent.getMarket_code_map().getOrDefault(row.getWhsalCd(), "Unknown Market")) // Null 체크
                .price(row.getCost())
                .del_unit(allSortingComponent.getUnit_code_map().get(String.valueOf(row.getDanq())))
                .quantity(row.getQty())
                .size(row.getSizeCd())
                .level(allSortingComponent.getLevel_code_map().getOrDefault(row.getLvCd(), "Unknown Level")) // Null 체크
                .build();
    }

    private AuctionEntity_crops convertRowToFishEntity (NewAuctionDataRow row, DetailCategoryEntity
            detailCategory){
        return AuctionEntity_crops.builder()
                .del_date(allSortingComponent.convertStrToLocalDate(row.getSaledate()))
                .large(detailCategory.getLarge())
                .middle(detailCategory.getMiddle())
                .small(detailCategory.getSmall())
                .product_name(detailCategory.getSmall())
                .market_name(allSortingComponent.getMarket_code_map().get(row.getWhsalCd()))
                .price(row.getCost())
                .del_unit(allSortingComponent.getUnit_code_map().get(String.valueOf(row.getDanq())))
                .quantity(row.getQty())
                .size(row.getSizeCd())
                .level(allSortingComponent.getLevel_code_map().get(row.getLvCd()))
                .build();

    }

    private AuctionEntity_crops convertRowToAniEntity (NewAuctionDataRow row, DetailCategoryEntity
            detailCategory){
        return AuctionEntity_crops.builder()
                .del_date(allSortingComponent.convertStrToLocalDate(row.getSaledate()))
                .large(detailCategory.getLarge())
                .middle(detailCategory.getMiddle())
                .small(detailCategory.getSmall())
                .product_name(detailCategory.getSmall())
                .market_name(allSortingComponent.getMarket_code_map().get(row.getWhsalCd()))
                .price(row.getCost())
                .del_unit(allSortingComponent.getUnit_code_map().get(String.valueOf(row.getDanq())))
                .quantity(row.getQty())
                .size(row.getSizeCd())
                .level(allSortingComponent.getLevel_code_map().get(row.getLvCd()))
                .build();

    }
}
