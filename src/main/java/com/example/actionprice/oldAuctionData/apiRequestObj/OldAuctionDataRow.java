package com.example.actionprice.oldAuctionData.apiRequestObj;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO 각 개체가 무엇인지 주석을 달아주려 했으니 너무 많아서 추후 진행
/**
 * @author 임호민
 * @created 24/10/02 11:47
 * @updated 24/10/02 11:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OldAuctionDataRow {
	@JsonProperty("ROW_NUM") // 열 번호
    private int ROW_NUM;

    @JsonProperty("DELNG_DE")  // 경락일자
    private String DELNG_DE;

    @JsonProperty("SBID_TIME") // 경매시간
    private String SBID_TIME;

    @JsonProperty("AUC_SE_CODE") // 경매구분코드
    private String AUC_SE_CODE;

    @JsonProperty("AUC_SE_NM") // 경배구분코드 명
    private String AUC_SE_NM;

    @JsonProperty("WHSAL_MRKT_NEW_CODE") // 시장코드
    private String WHSAL_MRKT_NEW_CODE;

    @JsonProperty("WHSAL_MRKT_NEW_NM") // 시장명
    private String WHSAL_MRKT_NEW_NM;

    @JsonProperty("WHSAL_MRKT_CODE") // 구시장 코드
    private String WHSAL_MRKT_CODE;

    @JsonProperty("WHSAL_MRKT_NM") // 구시장명
    private String WHSAL_MRKT_NM;

    @JsonProperty("CPR_INSTT_NEW_CODE") // 법인코드
    private String CPR_INSTT_NEW_CODE;

    @JsonProperty("INSTT_NEW_NM") // 법인명
    private String INSTT_NEW_NM;

    @JsonProperty("CPR_INSTT_CODE") // 구법인코드
    private String CPR_INSTT_CODE;

    @JsonProperty("INSTT_NM") //구법인 명
    private String INSTT_NM;

    @JsonProperty("LEDG_NO") // 경매원표번호
    private String LEDG_NO;

    @JsonProperty("SLE_SEQN") // 일련번호
    private String SLE_SEQN;

    @JsonProperty("CATGORY_NEW_CODE") // 부류코드
    private String CATGORY_NEW_CODE;

    @JsonProperty("CATGORY_NEW_NM") // 부류명
    private String CATGORY_NEW_NM;

    @JsonProperty("CATGORY_CODE") // 구부류코드
    private String CATGORY_CODE;

    @JsonProperty("CATGORY_NM") // 구부류명
    private String CATGORY_NM;

    @JsonProperty("STD_PRDLST_NEW_CODE") // 품목코드
    private String STD_PRDLST_NEW_CODE;

    @JsonProperty("STD_PRDLST_NEW_NM") // 품목명
    private String STD_PRDLST_NEW_NM;

    @JsonProperty("STD_PRDLST_CODE") // 구품목코드
    private String STD_PRDLST_CODE;

    @JsonProperty("STD_PRDLST_NM") // 구품목명
    private String STD_PRDLST_NM;

    @JsonProperty("STD_SPCIES_NEW_CODE") // 품종코드
    private String STD_SPCIES_NEW_CODE;

    @JsonProperty("STD_SPCIES_NEW_NM") // 품종명
    private String STD_SPCIES_NEW_NM;

    @JsonProperty("STD_SPCIES_CODE") // 구품종코드
    private String STD_SPCIES_CODE;

    @JsonProperty("STD_SPCIES_NM") // 구품종명
    private String STD_SPCIES_NM;

    @JsonProperty("DELNG_PRUT") // 거래단량
    private int DELNG_PRUT;

    @JsonProperty("STD_UNIT_NEW_CODE") //  단위코드
    private String STD_UNIT_NEW_CODE;

    @JsonProperty("STD_UNIT_NEW_NM") // 단위명
    private String STD_UNIT_NEW_NM;

    @JsonProperty("STD_FRMLC_NEW_CODE") // 포장상태코드
    private String STD_FRMLC_NEW_CODE;

    @JsonProperty("STD_FRMLC_NEW_NM") // 포장상태명
    private String STD_FRMLC_NEW_NM;

    @JsonProperty("STD_MG_NEW_CODE") // 크기코드
    private String STD_MG_NEW_CODE;

    @JsonProperty("STD_MG_NEW_NM") // 크기명
    private String STD_MG_NEW_NM;

    @JsonProperty("STD_QLITY_NEW_CODE") // 등급코드
    private String STD_QLITY_NEW_CODE;

    @JsonProperty("STD_QLITY_NEW_NM") // 등급명
    private String STD_QLITY_NEW_NM;

    @JsonProperty("CPR_USE_PRDLST_CODE") // 법인사용품목코드
    private String CPR_USE_PRDLST_CODE;

    @JsonProperty("CPR_USE_PRDLST_NM") // 법인사용품목명
    private String CPR_USE_PRDLST_NM;

    @JsonProperty("SBID_PRIC") // 거래가격
    private int SBID_PRIC;

    @JsonProperty("SHIPMNT_SE_CODE") // 출하구구분코드
    private String SHIPMNT_SE_CODE;

    @JsonProperty("SHIPMNT_SE_NM") // 출하구분명
    private String SHIPMNT_SE_NM;

    @JsonProperty("STD_MTC_NEW_CODE") // 산지코드
    private String STD_MTC_NEW_CODE;

    @JsonProperty("STD_MTC_NEW_NM") // 산지명
    private String STD_MTC_NEW_NM;

    @JsonProperty("CPR_MTC_CODE") // 구산지코드
    private String CPR_MTC_CODE;

    @JsonProperty("CPR_MTC_NM") // 구산지명
    private String CPR_MTC_NM;

    @JsonProperty("DELNG_QY") //거래량
    private int DELNG_QY;

}
