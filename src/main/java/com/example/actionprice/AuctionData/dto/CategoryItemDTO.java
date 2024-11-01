package com.example.actionprice.AuctionData.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CategoryItemDTO {
    private Long id; // 고유 ID
    private String name; // 이름
}
