package com.example.actionprice.originAuctionData;


import com.example.actionprice.originAuctionData.originApiRequestObj.OriginAuctionData;
import com.example.actionprice.originAuctionData.originApiRequestObj.OriginAuctionDataRow;
import com.example.actionprice.originAuctionData.originApiRequestObj.OriginAuctionDocument;
import com.example.actionprice.originAuctionData.originApiRequestObj.OriginAuctionItem;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Data
@Component
public class OriginAuctionDataFetcher {

    @Value("${lastAuctionData.url}")
    String lastAuctionUrl;

    @Value("${lastAuctionData.encodedKey}")
    String lastAuctionEncodedKey;

    private final WebClient webClient;
    private final Gson gson;


    public OriginAuctionDataFetcher() {
        this.webClient = WebClient.builder().build();
        this.gson = new Gson();
    }


    public ResponseEntity<String> getNewAuctionData_String(String regday) throws Exception {

        URI uri = composeUri(regday);

        ResponseEntity<String> responseEntity = webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class)
                .block();

        return responseEntity;
    }



    public OriginAuctionDocument getLastAuctionData_LastDocument(String regday) throws Exception {
        URI uri = composeUri(regday);

        System.out.println(uri);
        // 요청을 보내고 응답 받기
        String responseBody = webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        OriginAuctionDocument originAuctionDocument = gson.fromJson(responseBody, OriginAuctionDocument.class);

        return originAuctionDocument;
    }


    public Flux<OriginAuctionDataRow> getLastAuctionData_Flux(String regday) throws Exception {
        URI uri = composeUri(regday);

        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve() // 서버에 요청을 보내고, 응답을 받을 준비
                .bodyToMono(String.class) //응답 본문을 String 타입으로 받도록 지정
                .map(response -> {
                    try {
                        // 응답 JSON을 OriginAuctionDocument로 변환
                        return gson.fromJson(response, OriginAuctionDocument.class);
                    } catch (JsonSyntaxException e) {
                        // JSON 파싱 오류 처리
                        System.out.println("JSON 파싱 오류 발생: " + e.getMessage());
                        System.out.println("응답 JSON: " + response);
                        return new OriginAuctionDocument(); // 빈 객체 반환
                    }
                })
                .flatMapMany(document -> {
                    // document가 null이거나 데이터가 없는 경우 빈 Flux 반환
                    if (document.getData() != null && document.getData().getItem() != null) {
                        return Flux.fromIterable(document.getData().getItem());
                    } else {
                        return Flux.empty();
                    }
                });
    }

    private URI composeUri(String regday) throws URISyntaxException {

        String p_regday = regday; // 디폴트 값은 현재 날짜
        String p_product_cls_code = "02"; // 구분
        String p_country_code=""; // 지역
        String p_item_category_code= ""; // 부류코드
        String p_cert_id = "lhm3052@naver.com"; // 요청자 id
        String apiType = "json"; // 반환 형식
        String p_convert_kg_yn = "N"; // 중량 변환 여부

        String url = String.format("%s&p_product_cls_code=%s&p_country_code=%s&p_regday=%s&p_convert_kg_yn=%s&p_item_category_code=%s&p_cert_key=%s&p_cert_id=%s&p_returntype=%s",
                lastAuctionUrl,
                p_product_cls_code,
                p_country_code,
                p_regday,
                p_convert_kg_yn,
                p_item_category_code,
                lastAuctionEncodedKey,
                p_cert_id,
                apiType
        );
        return new URI(url);
    }




}



