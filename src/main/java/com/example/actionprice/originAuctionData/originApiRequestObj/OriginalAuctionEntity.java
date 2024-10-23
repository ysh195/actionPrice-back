package com.example.actionprice.originAuctionData.originApiRequestObj;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class OriginalAuctionEntity {

    @JsonProperty("item_name")
    private String itemName; // 품목명  (대분류)

    @JsonProperty("item_code")
    private String itemCode; // 품목코드

    @JsonProperty("kind_name")
    private String kindName; // 품종명 (중분류)

    @JsonProperty("kind_code")
    private String kindCode; // 품종코드

    @JsonProperty("rank")
    private String rank; // 상태(상품,중품)

    @JsonProperty("rank_code")
    private String rankCode; // 상태 코드

    @JsonProperty("unit")
    private String unit; // 단위

    @JsonProperty("day1")
    private String day1; // 1일자(조회일자 기준)

    @JsonProperty("dpr1")
    private String dpr1; // 조회일자 가격





}
