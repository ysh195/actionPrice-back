package com.example.actionprice.newAuctionData.newApiRequestObj;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewAuctionDataBody
{
    @JsonProperty("errorText")
    private String errorText;

    @JsonProperty("totCnt")
    private int totCnt;

    @JsonProperty("pageNo")
    private int pageNo;

    @JsonProperty("statusText")
    private String statusText;

    @JsonProperty("errorCode")
    private int errorCode;

    @JsonProperty("dataCnt")
    private int dataCnt;

    @JsonProperty("status")
    private String status;

    @JsonProperty("data")
    private List<NewAuctionDataRow> data;

}