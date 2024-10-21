package com.example.actionprice.lastAuctionData.lastApiRequestObj;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastAuctionCondition {

    @JsonProperty("LastAuctionItem")
    private LastAuctionItem item;
}
