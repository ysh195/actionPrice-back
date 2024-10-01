package com.example.actionprice.originalAuctionData.apiRequestObj;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionDataResultOfReq {
	
	@JsonProperty("code")
	private String code;    // 상태 코드
	
	@JsonProperty("message")
    private String message; // 메시지
}
