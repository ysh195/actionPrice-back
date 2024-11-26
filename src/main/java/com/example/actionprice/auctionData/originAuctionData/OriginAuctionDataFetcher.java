package com.example.actionprice.auctionData.originAuctionData;


import com.example.actionprice.auctionData.originAuctionData.originApiRequestObj.OriginAuctionDataRow;
import com.example.actionprice.auctionData.originAuctionData.originApiRequestObj.OriginAuctionDocument;
import com.google.gson.*;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
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

    /**
     * 메인데이터 추출(flux)
     * @param countryCode : 지역 코드
     * @param regday : 날짜
     * @param category_code : 대분류 코드
     * @author 연상훈
     * @created 2024-10-25 오전 12:03
     * @info api를 제공하는 측에서 "text" 타입으로만 데이터를 전달하고, 그 변형을 막아뒀기 때문에 무조건 일단은 text(String)으로 받고,
     * 그 다음에 jsonObject를 사용해서 데이터의 구조를 조사해야 함.
     * 바로 객체로 변환하지 않고 먼저 데이터의 구조를 조사하는 이유는,
     * jsonObject 내부에 data라는 객체의 타입이 무엇이냐에 따라 [조회데이터 없음] 오류가 발생하는지, 아니면 정상적으로 조회가 되는지가 달라짐
     * [조회데이터 없음] 오류 발생 시 이 메서드를 실행하는 것 자체에서부터 문제가 발생하기 때문에 이 메서드를 사용하는 로직 내에서는 처리하기가 까다로워짐
     * 그러니 여기서에서 문제를 다 해결해줘야 하는 것이고, jsonObject 조사를 통해 문제가 발생하지 않도록 대응하는 것.
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
                    JsonElement dataElement = jsonObject.get("data");
                    if (dataElement.isJsonArray()) {
                        JsonArray dataArray = dataElement.getAsJsonArray();
                        if (dataArray.size() > 0 && dataArray.get(0).isJsonPrimitive() && dataArray.get(0).getAsString().equals("001")) {
                            return Mono.empty();
                        }
                    }
                }
                OriginAuctionDocument document = gson.fromJson(str, OriginAuctionDocument.class);
                return Flux.fromIterable(document.getData().getItem());
            });
    }

    /**
     * 요청에 필요한 url을 구성하는 메서드
     * @author 연상훈
     */
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

        log.info(url);

        return new URI(url);
    }

}



