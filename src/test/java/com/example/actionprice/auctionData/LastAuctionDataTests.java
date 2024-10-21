package com.example.actionprice.auctionData;

import com.example.actionprice.lastAuctionData.LastAuctionDataFetcher;
import com.example.actionprice.lastAuctionData.lastApiRequestObj.LastAuctionDataRow;
import com.example.actionprice.lastAuctionData.lastApiRequestObj.LastAuctionDocument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class LastAuctionDataTests {

    @Autowired
    LastAuctionDataFetcher lastAuctionDataFetcher;


    @Test
    void lastAuctionDataPrintTest() throws Exception {
        ResponseEntity<String> responseEntity = lastAuctionDataFetcher.getNewAuctionData_String("20241010");
        System.out.println(responseEntity.toString());
    }

//
//    @Test
//    void lastAuctionDataTest() throws Exception {
//        LastAuctionDocument responseEntity = lastAuctionDataFetcher.getNewAuctionData_String("20241010");
//        List<LastAuctionDataRow> list =  responseEntity.getData().getRow();
//        list.stream().forEach(System.out::print);
//    }

}
