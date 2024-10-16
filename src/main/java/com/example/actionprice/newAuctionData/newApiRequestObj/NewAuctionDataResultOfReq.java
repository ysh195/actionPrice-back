package com.example.actionprice.newAuctionData.newApiRequestObj;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewAuctionDataResultOfReq {
    @JsonProperty("code")
    private String code;    // 상태 코드

    @JsonProperty("message")
    private String message; // 메시지
}
