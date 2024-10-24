package com.example.actionprice.originAuctionData;


import com.example.actionprice.originAuctionData.originApiRequestObj.OriginAuctionDataRow;
import com.example.actionprice.originAuctionData.originApiRequestObj.OriginAuctionDocument;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;


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
        ConnectionProvider provider = ConnectionProvider.builder("custom")
            .maxConnections(4000) // 최대 연결 수
            .pendingAcquireMaxCount(2000) // 대기 큐 최대 수
            .build();

        HttpClient httpClient = HttpClient.create(provider);

        this.webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();

        this.gson = new Gson();
    }

    private URI composeUri(String countryCode, String regday, String category_code) throws URISyntaxException {

        String p_cert_id = "lhm3052@naver.com"; // 요청자 id
        String apiType = "json"; // 반환 형식
        String p_convert_kg_yn = "Y"; // 중량 변환 여부

        String url = String.format("%s&p_country_code=%s&p_regday=%s&p_convert_kg_yn=Y&p_item_category_code=%s&p_cert_key=%s&p_cert_id=lhm3052@naver.com&p_returntype=json",
                lastAuctionUrl,
                countryCode,
                regday,
                category_code,
                lastAuctionEncodedKey
        );

        return new URI(url);
    }

    public String getAuctionData_String(String countryCode, String regday, String category_code) throws Exception {

        URI uri = composeUri(countryCode, regday, category_code);

        ResponseEntity<String> responseEntity = webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class)
                .block();

        return responseEntity.getBody();
    }

    /**
     * 메인데이터 추출(flux)
     * @param countryCode : 지역 코드
     * @param regday : 날짜
     * @param category_code : 대분류 코드
     * @author 연상훈
     * @created 2024-10-25 오전 12:03
     * @info
     */
    public Flux<OriginAuctionDataRow> getAuctionData_Flux(String countryCode, String regday, String category_code) throws Exception {
        URI uri = composeUri(countryCode, regday, category_code);

        return webClient.get()
            .uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(String.class)
            .flatMapMany(str -> {
                JsonElement jsonElement = JsonParser.parseString(str);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    // data가 object면(= data가 없으면)
                    if(!jsonObject.get("data").isJsonObject()){
                        return Mono.empty();
                    }
                }
                OriginAuctionDocument document = gson.fromJson(str, OriginAuctionDocument.class);
                return Flux.fromIterable(document.getData().getItem());
            });
    }


//
//    public OriginAuctionDocument getLastAuctionData_LastDocument(String regday) throws Exception {
//        URI uri = composeUri(regday);
//
//        System.out.println(uri);
//        // 요청을 보내고 응답 받기
//        String responseBody = webClient.get()
//                .uri(uri)
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//        OriginAuctionDocument originAuctionDocument = gson.fromJson(responseBody, OriginAuctionDocument.class);
//
//        return originAuctionDocument;
//    }
//
//    public Flux<OriginAuctionDataRow> getLastAuctionData_Flux(String regday) throws Exception {
//        URI uri = composeUri(regday);
//
//        return webClient.get()
//                .uri(uri)
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve() // 서버에 요청을 보내고, 응답을 받을 준비
//                .bodyToMono(String.class) //응답 본문을 String 타입으로 받도록 지정
//                .map(response -> {
//                    try {
//                        // 응답 JSON을 OriginAuctionDocument로 변환
//                        return gson.fromJson(response, OriginAuctionDocument.class);
//                    } catch (JsonSyntaxException e) {
//                        // JSON 파싱 오류 처리
//                        System.out.println("JSON 파싱 오류 발생: " + e.getMessage());
//                        System.out.println("응답 JSON: " + response);
//                        return new OriginAuctionDocument(); // 빈 객체 반환
//                    }
//                })
//                .flatMapMany(document -> {
//                    // document가 null이거나 데이터가 없는 경우 빈 Flux 반환
//                    if (document.getData() != null && document.getData().getItem() != null) {
//                        return Flux.fromIterable(document.getData().getItem());
//                    } else {
//                        return Flux.empty();
//                    }
//                });
//    }


}



