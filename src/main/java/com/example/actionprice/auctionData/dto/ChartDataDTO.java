package com.example.actionprice.auctionData.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 거래내역 조회 후 출력되는 그래프에 사용되는 dto
 * @author 연상훈
 * @info 프론트의 그래프 라이브러리인 rechart에 사용하기 위해서는 이러한 형태가 강제됨.
 * @see : 자의적인 수정 금지
 */
@AllArgsConstructor
@Getter
@ToString
public class ChartDataDTO {
  private String timeIntervals;
  private List<Map<String, Object>> chartDataList;
  private Set<String> countries;
}
