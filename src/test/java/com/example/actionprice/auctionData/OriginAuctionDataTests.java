package com.example.actionprice.auctionData;

import com.example.actionprice.AuctionData.detailCategory.AllSortingComponent;
import com.example.actionprice.AuctionData.service.AuctionEntityService;
import com.example.actionprice.originAuctionData.OriginAuctionDataFetcher;
import com.example.actionprice.originAuctionData.originApiRequestObj.OriginAuctionDataRow;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

@SpringBootTest
@Log4j2
public class OriginAuctionDataTests {


    @Autowired
    private OriginAuctionDataFetcher originAuctionDataFetcher;

    @Autowired
    private AllSortingComponent allSortingComponent;

    @Autowired
    private AuctionEntityService auctionEntityService;

    @Test
    @Disabled
    void auctionDataPrintTest() throws Exception {
        String reponseBody = originAuctionDataFetcher.getAuctionData_String("1101", "2024-10-20", "100");
        System.out.println(reponseBody);
    }

    @Test
    void auctionDataFluxTest() throws Exception {

        String year = "2024";
        String month = "01";
        int endDay = 31;

        for (int i = 1; i <= endDay; i++) {
            // 날짜 형식 맞추기
            String day = String.format("%02d", i);
            String date = String.format("%s-%s-%s", year, month, day);
            log.info("date : " + date);

            allSortingComponent.getMarket_code_map().entrySet().stream().forEach(marketCodeEntry -> {
                // key : 지역 코드, value : 지역 이름
                log.info("marketCode : " + marketCodeEntry.getValue());

                allSortingComponent.getGrand_sort().entrySet().stream().forEach(grandSortEntry -> {
                    // key : 대분류 코드, value : 대분류 이름
                    log.info("grandSort : " + grandSortEntry.getValue());
                  try {
                      log.info("flux");
                    Flux<OriginAuctionDataRow> flux = originAuctionDataFetcher.getAuctionData_Flux(marketCodeEntry.getKey(), date, grandSortEntry.getKey());
                    flux.subscribe(row -> {
                        log.info("flux - row");
                        log.info("row : " + row.toString());
                        auctionEntityService.saveEntityByCategory(row, date, marketCodeEntry.getValue(), grandSortEntry.getValue());
                    }, error -> {log.error("no data day");});
                  } catch (Exception e) {
                    throw new RuntimeException(e);
                  }
                });
            });

        }
    }

}
