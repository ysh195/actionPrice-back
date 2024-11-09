package com.example.actionprice.auctionData.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ChartDataElement {
  private Integer price;
  private int count;

  public ChartDataElement(Integer price) {
    this.price = price;
    this.count = 1;
  }

  public void stackData(ChartDataElement other){
    this.price += other.getPrice();
    this.count += other.getCount();
  }
}
