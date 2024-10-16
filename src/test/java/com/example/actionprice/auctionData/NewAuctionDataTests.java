package com.example.actionprice.auctionData;


import com.example.actionprice.AuctionData.detailCategory.AllSortingComponent;
import com.example.actionprice.AuctionData.entity.AuctionDataBaseEntity;
import com.example.actionprice.newAuctionData.newApiRequestObj.NewAuctionDataRow;
import com.example.actionprice.newAuctionData.NewAuctionDataFetcher;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

@SpringBootTest
@Log4j2
public class NewAuctionDataTests
{
    @Autowired
    NewAuctionDataFetcher newAuctionDataFetcher;

    @Autowired
    AllSortingComponent allSortingComponent;


    @Test
    void newAuctionDataPrintTest() throws Exception {
        ResponseEntity<String> responseEntity = newAuctionDataFetcher.getNewAuctionData_String("20241009");
        System.out.println(responseEntity.toString());
    }


}
