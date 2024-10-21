package com.example.actionprice.lastAuctionData.lastApiRequestObj;

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

    @JsonProperty("row")
    private List<LastAuctionDataRow> row;
}
