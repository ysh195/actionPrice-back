package com.example.actionprice.auctionData.dto;

import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ChartDataDTO {
  private String timeIntervals;
  private List<ChartDataListDTO> chartDataList;

  public ChartDataDTO(String timeIntervals, List<ChartDataListDTO> chartDataList) {
    this.timeIntervals = timeIntervals;
    this.chartDataList = chartDataList;
  }
}
