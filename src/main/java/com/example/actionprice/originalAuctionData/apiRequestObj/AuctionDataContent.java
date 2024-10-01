package com.example.actionprice.originalAuctionData.apiRequestObj;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionDataContent {
	
	@JsonProperty("totalCnt")
	private int totalCnt;
	
	@JsonProperty("startRow")
    private int startRow;
	
    @JsonProperty("endRow")
    private int endRow;
    
    @JsonProperty("result")
    private AuctionDataResultOfReq result;
    
    @JsonProperty("row")
    private List<AuctionDataRow> row;
}
