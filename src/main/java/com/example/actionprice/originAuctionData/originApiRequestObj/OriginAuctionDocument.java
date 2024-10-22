package com.example.actionprice.originAuctionData.originApiRequestObj;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OriginAuctionDocument {

    @JsonProperty("LastAuctionCondition")
    private OriginAuctionCondition condition;

    @JsonProperty("LastAuctionData")
    private OriginAuctionData data;
}
