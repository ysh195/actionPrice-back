package com.example.actionprice.auctionData;

import com.example.actionprice.originalAuctionData.LastAuctionDataFetcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class LastAuctionDataTests {

    @Autowired
    LastAuctionDataFetcher lastAuctionDataFetcher;


    @Test
    void lastAuctionDataPrintTest() throws Exception {
        ResponseEntity<String> responseEntity = lastAuctionDataFetcher.getNewAuctionData_String("2024-10-10");
        System.out.println(responseEntity.toString());
    }


//    @Test
//    void lastAuctionDataTest() throws Exception {
//        LastAuctionDocument responseEntity = lastAuctionDataFetcher.getLastAuctionData_LastDocument("2024-10-10");
//        List<LastAuctionDataRow> list =  responseEntity.getData().getItem();
//        list.stream().forEach(System.out::print);
//    }

}
