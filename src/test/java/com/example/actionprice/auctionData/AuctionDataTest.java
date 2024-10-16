package com.example.actionprice.auctionData;

import com.example.actionprice.AuctionData.detailCategory.AllSortingComponent;
import com.example.actionprice.AuctionData.entity.AuctionDataBaseEntity;
import com.example.actionprice.oldAuctionData.OldAuctionDataFetcher;
import com.example.actionprice.oldAuctionData.apiRequestObj.OldAuctionDataRow;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@SpringBootTest
@Log4j2
public class AuctionDataTest {

    @Autowired
    OldAuctionDataFetcher oldAuctionDataFetcher;

    @Autowired
    AllSortingComponent allSortingComponent;

    @Test
    @Disabled
    public void oldAuctionDataTest() throws Exception {

        String year = "2015";
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

                        String categoryCode = row.getCATGORY_NEW_CODE();
                        // String[] detailCategory = detailCategoryRepository.findById(categoryCode);
                        AuctionDataBaseEntity auctionDataBaseEntity = AuctionDataBaseEntity.builder()
                                .del_date(allSortingComponent.convertStrToLocalDate(row.getDELNG_DE()))
                                .large("detailCategory.getLarge()")
                                .middle("detailCategory.getMiddle()")
                                .small("detailCategory.getSmall()")
                                .product_name("detailCategory.getSmall()")
                                .market_name(allSortingComponent.getMarket_code_map().get(row.getWHSAL_MRKT_CODE()))
                                .price(row.getSBID_PRIC())
                                .del_unit(allSortingComponent.getUnit_code_map().get(row.getSTD_UNIT_NEW_CODE()))
                                .quantity(row.getDELNG_QY())
                                .size(row.getSTD_MG_NEW_NM())
                                .level(allSortingComponent.getUnit_code_map().get(row.getSTD_QLITY_NEW_CODE()))
                                .build();

                        return auctionDataBaseEntity;
                    } catch (Exception e) {
                        log.error(e);
                        return null;
                    }
                }).forEach(entity -> {
                    if(entity != null){
                        System.out.println(entity);
                    }
                });
            }
        }

    }

}
