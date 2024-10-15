package com.example.actionprice.auctionData;

import com.example.actionprice.AuctionData.AuctionDataRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Log4j2
public class AuctionDataTest {

    @Autowired
    private AuctionDataRepository auctionDataRepository;

    private final Map<String, String> market_code_map = Map.of("110001", "서울가락");
    private final Map<String, List<String>> unit_code_map = Map.of("10", List.of("농산물", "."));
    private final Map<String, List<String>> level_code_mpa = Map.of("10", List.of("농산물", "."));

    @Test
    public void auctionDataTest() {

    }
}
