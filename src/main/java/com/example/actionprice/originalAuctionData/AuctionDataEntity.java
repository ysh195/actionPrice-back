package com.example.actionprice.originalAuctionData;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

//TODO entity에 대한 논의 필요
/**
* @author 연상훈
* @created 24/10/01 21:10
* @updated 24/10/01 21:10
* @info entity에 대한 논의 필요. 지금 테이블 생성 권한이 없어서 만들려고 시도했다간 오류 생길 테니 주석 처리 해둠. 지금은 레포지토리도 만들면 안 됨
* @see : https://data.mafra.go.kr/opendata/data/indexOpenDataDetail.do?data_id=20151117000000000533
*/
// @Table(name = "auction_data")
// @Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class AuctionDataEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dataiId;

    private int rowNum; // 열 번호

    private LocalDateTime delngDe;  // 경락일자

    private String whsalMrktNewCode; // 시장코드

    private String whsalMrktNewNm; // 시장명

    private String whsalMrktCode; // 구시장 코드

    private String whsalMrktNm; // 구시장명

    private String catgoryNewCode; // 부류코드

    private String catgoryNewNm; // 부류명

    private String catgoryCode; // 구부류코드

    private String catgoryNm; // 구부류명

    private String stdPrdlstNewCode; // 품목코드

    private String stdPrdlstNewNm; // 품목명

    private String stdPrdlstCode; // 구품목코드

    private String stdPrdlstNm; // 구품목명

    private String stdSpciesNewCode; // 품종코드

    private String stdSpciesNewNm; // 품종명

    private String stdSpciesCode; // 구품종코드

    private String stdSpciesNm; // 구품종명

    private int delngPrut; // 거래단량

    private String stdUnitNewCode; //  단위코드

    private String stdUnitNewNm; // 단위명

    private String stdQlityNewCode; // 등급코드

    private String stdQlityNewNm; // 등급명

    private int sbidPric; // 거래가격

    private String stdMtcNewCode; // 산지코드

    private String stdMtcNewNm; // 산지명

    private String cprMtcCode; // 구산지코드

    private String cprMtcNm; // 구산지명

    private int delngQy; //거래량

}
