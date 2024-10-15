package com.example.actionprice.auctionData;

import com.example.actionprice.AuctionData.AuctionDataRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Log4j2
public class AuctionDataTest {

    @Autowired
    private AuctionDataRepository auctionDataRepository;

    private final Map<String, String> market_code_map = Map.ofEntries(
          Map.entry("110001","서울가락"),
          Map.entry("110008","서울강서"),
          Map.entry("210001","부산엄궁"),
          Map.entry("210005","부산국제수산"),
          Map.entry("210009","부산반여"),
          Map.entry("220001","대구북부"),
          Map.entry("230001","인천남촌"),
          Map.entry("230003","인천삼산"),
          Map.entry("240001","광주각화"),
          Map.entry("240004","광주서부"),
          Map.entry("250001","대전오정"),
          Map.entry("250003","대전노은"),
          Map.entry("310101","수원"),
          Map.entry("310401","안양"),
          Map.entry("310901","안산"),
          Map.entry("311201","구리"),
          Map.entry("320101","춘천"),
          Map.entry("320201","원주"),
          Map.entry("320301","강릉"),
          Map.entry("330101","청주"),
          Map.entry("330201","충주"),
          Map.entry("340101","천안"),
          Map.entry("350101","전주"),
          Map.entry("350301","익산"),
          Map.entry("350402","정읍"),
          Map.entry("360301","순천"),
          Map.entry("370101","포항"),
          Map.entry("370401","안동"),
          Map.entry("371501","구미"),
          Map.entry("380101","창원팔용"),
          Map.entry("380201","울산"),
          Map.entry("380303","창원내서"),
          Map.entry("380401","진주")
    );

    private final Map<String, List<String>> unit_code_map = Map.ofEntries(
            Map.entry("10",List.of("농산물", ".")),
            Map.entry("11",List.of("농산물", "g")),
            Map.entry("12",List.of("농산물", "kg")),
            Map.entry("13",List.of("농산물", "ton(M/T)")),
            Map.entry("14",List.of("농산물", "ml")),
            Map.entry("15",List.of("농산물", "L")),
            Map.entry("70",List.of("수산물", ".")),
            Map.entry("71",List.of("수산물", "g")),
            Map.entry("72",List.of("수산물", "kg")),
            Map.entry("73",List.of("수산물", "ton(M/T)")));

    private final Map<String, List<String>> level_code_mpa = Map.ofEntries(
            Map.entry("10",List.of("농산물", ".")),
            Map.entry("11",List.of("농산물", "특")),
            Map.entry("12",List.of("농산물", "상")),
            Map.entry("13",List.of("농산물", "보통")),
            Map.entry("14",List.of("농산물", "4등")),
            Map.entry("15",List.of("농산물", "5등")),
            Map.entry("16",List.of("농산물", "6등")),
            Map.entry("17",List.of("농산물", "7등")),
            Map.entry("18",List.of("농산물", "8등")),
            Map.entry("19",List.of("농산물", "등외")),
            Map.entry("1A",List.of("농산물", "유기농산물")),
            Map.entry("1B",List.of("농산물", "유기농산물(전환기)")),
            Map.entry("1C",List.of("농산물", "무농약농산물")),
            Map.entry("1D",List.of("농산물", "저농약농산물")),
            Map.entry("1E",List.of("농산물", "유기축산물")),
            Map.entry("1F",List.of("농산물", "유기축산물(전환기)")),
            Map.entry("1G",List.of("농산물", "무항생제축산물")),
            Map.entry("1Y",List.of("농산물", "혼합")),
            Map.entry("1Z",List.of("농산물", "무등급")),
            Map.entry("70",List.of("수산물", ".")),
            Map.entry("71",List.of("수산물", "자연산 특")),
            Map.entry("72",List.of("수산물", "자연산 상")),
            Map.entry("73",List.of("수산물", "자연산 보통")),
            Map.entry("74",List.of("수산물", "자연산 하")),
            Map.entry("75",List.of("수산물", "양식산 특")),
            Map.entry("76",List.of("수산물", "양식산 상")),
            Map.entry("77",List.of("수산물", "양식산 보통")),
            Map.entry("78",List.of("수산물", "양식산 하")),
            Map.entry("79",List.of("수산물", "등외")),
            Map.entry("7A",List.of("수산물", "특")),
            Map.entry("7B",List.of("수산물", "상")),
            Map.entry("7C",List.of("수산물", "보통")),
            Map.entry("7D",List.of("수산물", "하")),
            Map.entry("7Z",List.of("수산물", "무등급")));

    @Test
    public void auctionDataTest() {

    }
}
