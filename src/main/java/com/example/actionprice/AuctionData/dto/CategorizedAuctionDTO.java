package com.example.actionprice.AuctionData.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CategorizedAuctionDTO {

    private Map<String, Map<String, List<String>>> categorizedAuctions;

    public CategorizedAuctionDTO(Map<String, Map<String, List<String>>> categorizedAuctions) {
        this.categorizedAuctions = categorizedAuctions;
    }
}
