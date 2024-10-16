package com.example.actionprice.newAuctionData.newApiRequestObj;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewAuctionDataContent
{

    @JsonProperty("totalCnt")
    private int totalCnt;

    @JsonProperty("startRow")
    private int startRow;

    @JsonProperty("endRow")
    private int endRow;

    @JsonProperty("result")
    private NewAuctionDataResultOfReq result;

    @JsonProperty("row")
    private List<NewAuctionDataRow> row;


}
