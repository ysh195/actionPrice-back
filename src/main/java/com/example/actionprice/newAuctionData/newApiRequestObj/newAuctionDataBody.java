package com.example.actionprice.newAuctionData.newApiRequestObj;

import com.example.actionprice.oldAuctionData.apiRequestObj.OldAuctionDataContent;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class newAuctionDataBody
{
    @JsonProperty("")
    private OldAuctionDataContent content;
}