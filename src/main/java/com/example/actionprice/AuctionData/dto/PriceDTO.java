package com.example.actionprice.AuctionData.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;


@Getter
@Builder
public class PriceDTO {

    private String large;
    private String middle;
    private String small;
    private String rank;
    private double averagePrice;
    private LocalDate startDate;
    private LocalDate endDate;

}
