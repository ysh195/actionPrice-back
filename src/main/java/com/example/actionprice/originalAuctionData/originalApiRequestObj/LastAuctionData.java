package com.example.actionprice.originalAuctionData.originalApiRequestObj;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LastAuctionData {

    @JsonProperty("error_code")
    private String error_code;

    @JsonProperty("item")
    private List<LastAuctionDataRow> item;
}
