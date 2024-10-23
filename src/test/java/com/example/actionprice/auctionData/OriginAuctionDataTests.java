package com.example.actionprice.auctionData;

import com.example.actionprice.AuctionData.detailCategory.AllSortingComponent;
import com.example.actionprice.AuctionData.entity.*;
import com.example.actionprice.AuctionData.repository.*;
import com.example.actionprice.originAuctionData.OriginAuctionDataFetcher;
import com.example.actionprice.originAuctionData.originApiRequestObj.OriginAuctionDataRow;
import com.example.actionprice.originAuctionData.originApiRequestObj.OriginAuctionDocument;
import com.example.actionprice.originAuctionData.originApiRequestObj.OriginAuctionItem;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

import java.util.List;

@SpringBootTest
@Log4j2
public class OriginAuctionDataTests {


    @Autowired
    private OriginAuctionDataFetcher originAuctionDataFetcher;

    @Autowired
    private AllSortingComponent allSortingComponent;

    @Autowired
    AniEntity_repo aniEntity_repo;

    @Autowired
    FishEntity_repo fishEntity_repo;

    @Autowired
    FoodCropsEntity_repo foodCropsEntity_repo;

    @Autowired
    FruitEntity_repo fruitEntity_repo;

    @Autowired
    ProduceEntity_repo produceEntity_repo;

    @Autowired
    SpecialCropsEntity_repo specialCropsEntity_repo;



    @Disabled
    @Test
    void lastAuctionDataPrintTest() throws Exception {
        ResponseEntity<String> responseEntity = originAuctionDataFetcher.getNewAuctionData_String("2024-10-10");
        System.out.println(responseEntity.toString());
    }

    @Test
    void auctionDataBodyTest() throws Exception {
        // API 호출로부터 데이터를 받아옴
        OriginAuctionDocument responseEntity = originAuctionDataFetcher.getLastAuctionData_LastDocument("2024-10-10");
        // Null 체크 및 데이터 확인
        if (responseEntity == null) {
            System.out.println("API 응답이 없습니다.");
            return;
        }

        // 데이터를 출력
        List<OriginAuctionDataRow> list = responseEntity.getData().getItem();
        list.stream().forEach(System.out::println);
    }

    @Disabled
    @Test
    void auctionDataFluxTest() throws Exception {

        String year = "2024";
        String month = "10";

        int endDay = 10;

        for (int i=1; i<=endDay; i++){
            String day = String.valueOf(i);
            if (day.length() == 1){
                day = "0"+day;
            }

            String date = String.format("%s-%s-%s", year, month, day);

                Flux<OriginAuctionDataRow> auctionDataFlux = originAuctionDataFetcher.getLastAuctionData_Flux(date);
                auctionDataFlux.toStream().forEach(System.out::print);
        }

    }

    @Disabled
    @Test
    void originalAuctionDataTest() throws Exception {

        String year = "2024";
        String month = "02";

        int endDay = 1; //각 달의 마지막 적기

        for(int i=1; i<=endDay; i++) {
            String day = String.valueOf(i);
            if (day.length() == 1) {
                day = "0" + day;
            }
            OriginAuctionItem category = new OriginAuctionItem();
            String date = String.format("%s-%s-%s", year, month, day);
            Flux<OriginAuctionDataRow> auctionDataRowFlux = originAuctionDataFetcher.getLastAuctionData_Flux(date);
            auctionDataRowFlux.toStream().map(row -> {
                try{

                    String categoryCode = category.getCategoryCode();  // 카테고리 코드 가져오기
                    String grandSort = allSortingComponent.getGrand_sort().get(categoryCode);  // 부류코드 가져오기

                    if(grandSort!=null){
                        switch (grandSort){
                            case "100":
                                return convertRowToFoodCropsEntity(row,category);
                            case "200":
                                return convertRowToProduceEntity(row,category);
                            case "300":
                                return convertRowToSpecialCropEntity(row,category);
                            case "400":
                                return convertRowToFruitEntity(row,category);
                            case "500":
                                return convertRowToAniEntity(row,category);
                            case "600":
                                return convertRowToFishEntity(row,category);
                            default:
                                System.out.println(grandSort + "는 존재하지 않는 항목입니다.");
                                break;
                        }
                    }else{
                        System.out.println("카테고리 not found");
                    }

                }
                catch (Exception e)
                {
                    log.error(e);
                    return null;
                }
                return null;
            }).forEach(entity -> {
                if(entity != null){
                    String categoryCode = category.getCategoryCode();
                    String grandSort = allSortingComponent.getGrand_sort().get(categoryCode);
                    switch (grandSort){
                        case "100":
                            System.out.println(entity.toString());
                            foodCropsEntity_repo.save((AuctionEntity_foodCrops) entity);
                            break;
                        case "200":
                            System.out.println(entity.toString());
                            produceEntity_repo.save((AuctionEntity_produce) entity);
                            break;
                        case "300":
                            System.out.println(entity.toString());
                            specialCropsEntity_repo.save((AuctionEntity_specialCrop) entity);
                            break;
                        case "400":
                            System.out.println(entity.toString());
                            fruitEntity_repo.save((AuctionEntity_fruit) entity);
                            break;
                        case "500":
                            System.out.println(entity.toString());
                            aniEntity_repo.save((AuctionEntity_ani) entity);
                            break;
                        case "600":
                            System.out.println(entity.toString());
                            fishEntity_repo.save((AuctionEntity_fish) entity);
                            break;
                        default:
                            System.out.println(grandSort + "는 존재하지 않는 항목입니다.");
                            break;
                    }
                }
            });
        }

    }

    private AuctionEntity_ani convertRowToAniEntity (OriginAuctionDataRow row, OriginAuctionItem item){
        return AuctionEntity_ani.builder()
                .large(row.getItemName())
                .middle(row.getKindName())
                .product_name(allSortingComponent.getMarket_code_map().get(item.getCountryCode()))
                .del_date(item.getRegDay())
                .price(row.getDpr1())
                .build();
    }

    private AuctionEntity_fish convertRowToFishEntity (OriginAuctionDataRow row, OriginAuctionItem item){
        return AuctionEntity_fish.builder()
                .large(row.getItemName())
                .middle(row.getKindName())
                .product_name(allSortingComponent.getMarket_code_map().get(item.getCountryCode()))
                .del_date(item.getRegDay())
                .price(row.getDpr1())
                .build();
    }

    private AuctionEntity_foodCrops convertRowToFoodCropsEntity (OriginAuctionDataRow row, OriginAuctionItem item){
        return AuctionEntity_foodCrops.builder()
                .large(row.getItemName())
                .middle(row.getKindName())
                .product_name(allSortingComponent.getMarket_code_map().get(item.getCountryCode()))
                .del_date(item.getRegDay())
                .price(row.getDpr1())
                .build();
    }

    private AuctionEntity_fruit convertRowToFruitEntity (OriginAuctionDataRow row, OriginAuctionItem item){
        return AuctionEntity_fruit.builder()
                .large(row.getItemName())
                .middle(row.getKindName())
                .product_name(allSortingComponent.getMarket_code_map().get(item.getCountryCode()))
                .del_date(item.getRegDay())
                .price(row.getDpr1())
                .build();
    }

    private AuctionEntity_produce convertRowToProduceEntity (OriginAuctionDataRow row, OriginAuctionItem item){
        return AuctionEntity_produce.builder()
                .large(row.getItemName())
                .middle(row.getKindName())
                .product_name(allSortingComponent.getMarket_code_map().get(item.getCountryCode()))
                .del_date(item.getRegDay())
                .price(row.getDpr1())
                .build();
    }

    private AuctionEntity_specialCrop convertRowToSpecialCropEntity (OriginAuctionDataRow row, OriginAuctionItem item){
        return AuctionEntity_specialCrop.builder()
                .large(row.getItemName())
                .middle(row.getKindName())
                .product_name(allSortingComponent.getMarket_code_map().get(item.getCountryCode()))
                .del_date(item.getRegDay())
                .price(row.getDpr1())
                .build();
    }


}
