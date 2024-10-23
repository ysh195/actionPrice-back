package com.example.actionprice.originAuctionData;

import com.example.actionprice.originAuctionData.originApiRequestObj.OriginAuctionDataRow;
import com.example.actionprice.originAuctionData.originApiRequestObj.OriginAuctionDocument;
import com.google.gson.Gson;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.net.URISyntaxException;


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
                .retrieve()
                .bodyToMono(OriginAuctionDocument.class)
                .flatMapMany(body -> Flux.fromIterable(body.getData().getItem()));
    }


    private URI composeUri(String regday) throws URISyntaxException {

        String p_regday = regday; // 디폴트 값은 현재 날짜
        String p_product_cls_code = "02"; // 구분
        String p_country_code=""; // 지역
        String p_item_category_code=""; // 부류코드
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



