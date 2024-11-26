package com.example.actionprice.auctionData.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

/**
 * 카테고리 정보 전달에 사용되는 dto
 */
@Getter
@Builder
public class CategoryDTO {
    private String large;
    private String middle;
    private String small;
    private String rank;
    private List<CategoryItemDTO> list;
}
