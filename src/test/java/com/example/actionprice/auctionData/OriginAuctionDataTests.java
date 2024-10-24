package com.example.actionprice.auctionData;

import com.example.actionprice.AuctionData.detailCategory.AllSortingComponent;
import com.example.actionprice.AuctionData.entity.*;
import com.example.actionprice.AuctionData.repository.*;
import com.example.actionprice.originAuctionData.OriginAuctionDataFetcher;
import com.example.actionprice.originAuctionData.originApiRequestObj.OriginAuctionData;
import com.example.actionprice.originAuctionData.originApiRequestObj.OriginAuctionDataRow;
import com.example.actionprice.originAuctionData.originApiRequestObj.OriginAuctionDocument;
import com.example.actionprice.originAuctionData.originApiRequestObj.OriginAuctionItem;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

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
    @Autowired
    private Gson gson;


    @Disabled
    @Test
    void lastAuctionDataPrintTest() throws Exception {
        ResponseEntity<String> responseEntity = originAuctionDataFetcher.getNewAuctionData_String("2024-10-10");
        System.out.println(responseEntity.toString());
    }



    @Disabled
    @Test
    void auctionDataBodyTest() throws Exception {
        // API 호출로부터 데이터를 받아옴
        OriginAuctionDocument responseEntity = originAuctionDataFetcher.getLastAuctionData_LastDocument("2024-10-01");
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
        int endDay = 20;

        for (int i = 1; i <= endDay; i++) {
            // 날짜 형식 맞추기
            String day = String.format("%02d", i);
            String date = String.format("%s-%s-%s", year, month, day);

            System.out.println("테스트 중인 날짜: " + date);

            // Flux로 데이터 받아옴
            Flux<OriginAuctionDataRow> auctionDataFlux = originAuctionDataFetcher.getLastAuctionData_Flux(date);

            // Flux에서 데이터를 스트림으로 변환 후 출력
            List<OriginAuctionDataRow> auctionDataList = auctionDataFlux.collectList().block();

            if (auctionDataList != null && !auctionDataList.isEmpty()) {
                auctionDataList.forEach(item -> {
                    System.out.println(item);  // 데이터가 있을 경우 출력
                });
            } else {
                System.out.println("빈 데이터 또는 오류 발생: " + date);  // 데이터가 없거나 오류 발생 시 출력
            }
        }
    }

    @Test
    void originalAuctionDataTest() throws Exception {
        String year = "2024";
        String month = "10";
        int endDay = 5; // 각 달의 마지막 적기

        for (int i = 1; i <= endDay; i++) {
            String day = String.format("%02d", i);
            OriginAuctionItem category = new OriginAuctionItem();

            category.setP_category_code("100"); // 실제 카테고리 코드로 초기화

            String date = String.format("%s-%s-%s", year, month, day);
            Flux<OriginAuctionDataRow> auctionDataRowFlux = originAuctionDataFetcher.getLastAuctionData_Flux(date);

            auctionDataRowFlux.toStream().map(row -> {
                try {
                    String categoryCode = category.getP_category_code();  // 카테고리 코드 가져오기
                    String grandSort = allSortingComponent.getGrand_sort().get(categoryCode);  // 부류코드 가져오기

                    if (grandSort != null) {
                        System.out.println("Processing grandSort: " + grandSort); // grandSort 로그 추가
                        return createEntityBasedOnGrandSort(grandSort, row, category);
                    } else {
                        System.out.println("카테고리 not found: " + categoryCode);
                    }
                } catch (Exception e) {
                    log.error(e);
                    return null;
                }
                return null;
            }).forEach(entity -> {
                if (entity != null) {
                    saveEntityBasedOnGrandSort(entity);
                }
            });
        }
    }

    private AuctionBaseEntity createEntityBasedOnGrandSort(String grandSort, OriginAuctionDataRow row, OriginAuctionItem item) {
        System.out.println("Processing grandSort: " + grandSort); // grandSort 값 확인 로그 추가
        switch (grandSort) {
            case "식량작물":
                return convertRowToFoodCropsEntity(row, item);
            case "채소류":
                return convertRowToProduceEntity(row, item);
            case "특용작물":
                return convertRowToSpecialCropEntity(row, item);
            case "과일류":
                return convertRowToFruitEntity(row, item);
            case "축산물":
                return convertRowToAniEntity(row, item);
            case "수산물":
                return convertRowToFishEntity(row, item);
            default:
                System.out.println(grandSort + "는 존재하지 않는 항목입니다.");
                return null;
        }
    }

    private void saveEntityBasedOnGrandSort(AuctionBaseEntity entity) {
        if (entity instanceof AuctionEntity_foodCrops) {
            AuctionEntity_foodCrops savedEntity = foodCropsEntity_repo.save((AuctionEntity_foodCrops) entity);
            System.out.println("Saved FoodCrops Entity ID: " + savedEntity.getDel_id()); //pk 확인 로그 추가
        } else if (entity instanceof AuctionEntity_produce) {
            AuctionEntity_produce savedEntity = produceEntity_repo.save((AuctionEntity_produce) entity);
            System.out.println("Saved Produce Entity ID: " + savedEntity.getDel_id());
        } else if (entity instanceof AuctionEntity_specialCrop) {
            AuctionEntity_specialCrop savedEntity = specialCropsEntity_repo.save((AuctionEntity_specialCrop) entity);
            System.out.println("Saved SpecialCrop Entity ID: " + savedEntity.getDel_id());
        } else if (entity instanceof AuctionEntity_fruit) {
            AuctionEntity_fruit savedEntity = fruitEntity_repo.save((AuctionEntity_fruit) entity);
            System.out.println("Saved Fruit Entity ID: " + savedEntity.getDel_id());
        } else if (entity instanceof AuctionEntity_ani) {
            AuctionEntity_ani savedEntity = aniEntity_repo.save((AuctionEntity_ani) entity);
            System.out.println("Saved Ani Entity ID: " + savedEntity.getDel_id());
        } else if (entity instanceof AuctionEntity_fish) {
            AuctionEntity_fish savedEntity = fishEntity_repo.save((AuctionEntity_fish) entity);
            System.out.println("Saved Fish Entity ID: " + savedEntity.getDel_id());
        } else {
            System.out.println("Unknown entity type");
        }
    }

    // 엔티티 생성을 위한 공통 메소드
    private AuctionBaseEntity createAuctionEntity(AuctionBaseEntity.AuctionBaseEntityBuilder<?, ?> builder, OriginAuctionDataRow row, OriginAuctionItem item) {
        System.out.println("Creating entity with item_name: " + row.getItem_name()); // 생성 로그 추가
        Map<String,String> grand_sort = allSortingComponent.getGrand_sort();
        Map<String,String> market_code_map = allSortingComponent.getMarket_code_map();
        AuctionBaseEntity entity = builder
                .product_name(grand_sort.get(item.getP_category_code()))
                .large(row.getItem_name())
                .middle(row.getKind_name())
                .market_name(market_code_map.get(item.getP_country_code()))
                .del_date(allSortingComponent.convertStrToLocalDate(item.getP_regday()))
                .price(Integer.parseInt(row.getDpr1()))
                .build();
        System.out.println("Entity created: " + entity); // 엔티티 생성 확인 로그 추가
        return entity;
    }

    // 각 엔티티 변환 메소드
    private AuctionEntity_ani convertRowToAniEntity(OriginAuctionDataRow row, OriginAuctionItem item) {
        return (AuctionEntity_ani) createAuctionEntity(AuctionEntity_ani.builder(), row, item);
    }

    private AuctionEntity_fish convertRowToFishEntity(OriginAuctionDataRow row, OriginAuctionItem item) {
        return (AuctionEntity_fish) createAuctionEntity(AuctionEntity_fish.builder(), row, item);
    }

    private AuctionEntity_foodCrops convertRowToFoodCropsEntity(OriginAuctionDataRow row, OriginAuctionItem item) {
        return (AuctionEntity_foodCrops) createAuctionEntity(AuctionEntity_foodCrops.builder(), row, item);
    }

    private AuctionEntity_fruit convertRowToFruitEntity(OriginAuctionDataRow row, OriginAuctionItem item) {
        return (AuctionEntity_fruit) createAuctionEntity(AuctionEntity_fruit.builder(), row, item);
    }

    private AuctionEntity_produce convertRowToProduceEntity(OriginAuctionDataRow row, OriginAuctionItem item) {
        return (AuctionEntity_produce) createAuctionEntity(AuctionEntity_produce.builder(), row, item);
    }

    private AuctionEntity_specialCrop convertRowToSpecialCropEntity(OriginAuctionDataRow row, OriginAuctionItem item) {
        return (AuctionEntity_specialCrop) createAuctionEntity(AuctionEntity_specialCrop.builder(), row, item);
    }


}
