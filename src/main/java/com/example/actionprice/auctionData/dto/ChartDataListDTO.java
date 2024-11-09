package com.example.actionprice.auctionData.dto;

import java.time.LocalDate;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class ChartDataListDTO implements Comparable<ChartDataListDTO> {
  private LocalDate baseDay;
  private String country;
  private int averagePrice;

  @Override
  public int compareTo(ChartDataListDTO other) {
    return this.baseDay.compareTo(other.baseDay);
  }

  @Override
  public boolean equals(Object o) {
    return this.hashCode() == o.hashCode();
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseDay, country);
  }
}
