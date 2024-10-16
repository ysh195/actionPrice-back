package com.example.actionprice.auctionData;

import com.example.actionprice.AuctionData.detailCategory.AllSortingComponent;
import com.example.actionprice.AuctionData.detailCategory.DetailCategoryEntity;
import com.example.actionprice.AuctionData.detailCategory.DetailCategoryRepository;
import com.example.actionprice.AuctionData.entity.AuctionEntity_ani;
import com.example.actionprice.AuctionData.entity.AuctionEntity_crops;
import com.example.actionprice.AuctionData.entity.AuctionEntity_fish;
import com.example.actionprice.AuctionData.repository.AniEntity_repo;
import com.example.actionprice.AuctionData.repository.CropsEntity_repo;
import com.example.actionprice.AuctionData.repository.FishEntity_repo;
import com.example.actionprice.oldAuctionData.OldAuctionDataFetcher;
import com.example.actionprice.oldAuctionData.apiRequestObj.OldAuctionDataRow;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
@Log4j2
public class AuctionDataTest {

    @Autowired
    private OldAuctionDataFetcher oldAuctionDataFetcher;

    @Autowired
    private AllSortingComponent allSortingComponent;

    @Autowired
    private DetailCategoryRepository detailCategoryRepository;

    @Autowired
    AniEntity_repo aniEntity_repo;

    @Autowired
    CropsEntity_repo cropsEntity_repo;

    @Autowired
    FishEntity_repo fishEntity_repo;

    /**
     * 구 API로 메인데이터를 추출하여 저장하기 위한 로직
     * @author 연상훈
     * @created 2024-10-16 오후 2:31
     * @updated 2024-10-16 오후 2:31
     * @param : year = 데이터를 가져올 년도
     * @param : month = 데이터를 가져올 월
     * @parma : endDay = 데이터를 가져올 달의 마지막 일자.
     * @see : endDay를 직접 조사해서 넣을 것
     */
    @Test
    void oldAuctionDataTest() throws Exception {

        String year = "2023";
        String month = "08";

        int endDay = 1; // 각 달의 마지막 날이 언제인지 확인 후 입력

        for (int i=1; i<=endDay; i++){
            String day = String.valueOf(i);
            if (day.length() == 1){
                day = "0"+day;
            }

            String date = String.format("%s%s%s", year, month, day);

            for(String marketName : allSortingComponent.getOld_market_name_Arr()){

                Flux<OldAuctionDataRow> auctionDataFlux = oldAuctionDataFetcher.getOriginalAuctionData_Flux(date, marketName);

                auctionDataFlux.toStream().map(row -> {
                    try{

                        DetailCategoryEntity detailCategory = detailCategoryRepository.findById(row.getSTD_SPCIES_NEW_CODE()).orElse(null);
                        if(detailCategory != null){
                            String grandSort = allSortingComponent.getGrand_category_map().get(detailCategory.getLarge());

                            switch (grandSort){
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

                        }
                        else{
                            System.out.println("Category not found");
                            return null;
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

    }

    private AuctionEntity_crops convertRowToCropsEntity(OldAuctionDataRow row, DetailCategoryEntity detailCategory){
        return AuctionEntity_crops.builder()
                .del_date(allSortingComponent.convertStrToLocalDate(row.getDELNG_DE()))
                .large(detailCategory.getLarge())
                .middle(detailCategory.getMiddle())
                .small(detailCategory.getSmall())
                .product_name(detailCategory.getSmall())
                .market_name(allSortingComponent.getMarket_code_map().get(row.getWHSAL_MRKT_CODE()))
                .price(row.getSBID_PRIC())
                .del_unit(allSortingComponent.getUnit_code_map().get(row.getSTD_UNIT_NEW_CODE()))
                .quantity(row.getDELNG_QY())
                .size(row.getSTD_MG_NEW_NM())
                .level(allSortingComponent.getUnit_code_map().get(row.getSTD_QLITY_NEW_CODE()))
                .build();
    }

    private AuctionEntity_fish convertRowToFishEntity(OldAuctionDataRow row, DetailCategoryEntity detailCategory){
        return AuctionEntity_fish.builder()
                .del_date(allSortingComponent.convertStrToLocalDate(row.getDELNG_DE()))
                .large(detailCategory.getLarge())
                .middle(detailCategory.getMiddle())
                .small(detailCategory.getSmall())
                .product_name(detailCategory.getSmall())
                .market_name(allSortingComponent.getMarket_code_map().get(row.getWHSAL_MRKT_CODE()))
                .price(row.getSBID_PRIC())
                .del_unit(allSortingComponent.getUnit_code_map().get(row.getSTD_UNIT_NEW_CODE()))
                .quantity(row.getDELNG_QY())
                .size(row.getSTD_MG_NEW_NM())
                .level(allSortingComponent.getUnit_code_map().get(row.getSTD_QLITY_NEW_CODE()))
                .build();
    }

    private AuctionEntity_ani convertRowToAniEntity(OldAuctionDataRow row, DetailCategoryEntity detailCategory){
        return AuctionEntity_ani.builder()
                .del_date(allSortingComponent.convertStrToLocalDate(row.getDELNG_DE()))
                .large(detailCategory.getLarge())
                .middle(detailCategory.getMiddle())
                .small(detailCategory.getSmall())
                .product_name(detailCategory.getSmall())
                .market_name(allSortingComponent.getMarket_code_map().get(row.getWHSAL_MRKT_CODE()))
                .price(row.getSBID_PRIC())
                .del_unit(allSortingComponent.getUnit_code_map().get(row.getSTD_UNIT_NEW_CODE()))
                .quantity(row.getDELNG_QY())
                .size(row.getSTD_MG_NEW_NM())
                .level(allSortingComponent.getUnit_code_map().get(row.getSTD_QLITY_NEW_CODE()))
                .build();
    }

}
