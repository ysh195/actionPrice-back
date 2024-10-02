package com.example.actionprice.originalAuctionData.apiRequestObj;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO 각 개체가 무엇인지 주석을 달아주려 했으니 너무 많아서 추후 진행
/**
 * @author 연상훈
 * @created 24/10/01 20:10
 * @updated 24/10/02 11:35
 * @info api에서 제공하는 객체 형태 그대로 가져옴
 * @see : https://data.mafra.go.kr/opendata/data/indexOpenDataDetail.do?data_id=20151117000000000533
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuctionDataRow {
	@JsonProperty("ROW_NUM")
    private int rowNum;

    @JsonProperty("DELNG_DE")
    private String delngDe;

    @JsonProperty("SBID_TIME")
    private String sbidTime;

    @JsonProperty("AUC_SE_CODE")
    private String aucSeCode;

    @JsonProperty("AUC_SE_NM")
    private String aucSeNm;

    @JsonProperty("WHSAL_MRKT_NEW_CODE")
    private String whsalMrktNewCode;

    @JsonProperty("WHSAL_MRKT_NEW_NM")
    private String whsalMrktNewNm;

    @JsonProperty("WHSAL_MRKT_CODE")
    private String whsalMrktCode;

    @JsonProperty("WHSAL_MRKT_NM")
    private String whsalMrktNm;

    @JsonProperty("CPR_INSTT_NEW_CODE")
    private String cprInsttNewCode;

    @JsonProperty("INSTT_NEW_NM")
    private String insttNewNm;

    @JsonProperty("CPR_INSTT_CODE")
    private String cprInsttCode;

    @JsonProperty("INSTT_NM")
    private String insttNm;

    @JsonProperty("LEDG_NO")
    private String ledgNo;

    @JsonProperty("SLE_SEQN")
    private String sleSeqn;

    @JsonProperty("CATGORY_NEW_CODE")
    private String catgoryNewCode;

    @JsonProperty("CATGORY_NEW_NM")
    private String catgoryNewNm;

    @JsonProperty("CATGORY_CODE")
    private String catgoryCode;

    @JsonProperty("CATGORY_NM")
    private String catgoryNm;

    @JsonProperty("STD_PRDLST_NEW_CODE")
    private String stdPrdlstNewCode;

    @JsonProperty("STD_PRDLST_NEW_NM")
    private String stdPrdlstNewNm;

    @JsonProperty("STD_PRDLST_CODE")
    private String stdPrdlstCode;

    @JsonProperty("STD_PRDLST_NM")
    private String stdPrdlstNm;

    @JsonProperty("STD_SPCIES_NEW_CODE")
    private String stdSpciesNewCode;

    @JsonProperty("STD_SPCIES_NEW_NM")
    private String stdSpciesNewNm;

    @JsonProperty("STD_SPCIES_CODE")
    private String stdSpciesCode;

    @JsonProperty("STD_SPCIES_NM")
    private String stdSpciesNm;

    @JsonProperty("DELNG_PRUT")
    private int delngPrut;

    @JsonProperty("STD_UNIT_NEW_CODE")
    private String stdUnitNewCode;

    @JsonProperty("STD_UNIT_NEW_NM")
    private String stdUnitNewNm;

    @JsonProperty("STD_FRMLC_NEW_CODE")
    private String stdFrmlcNewCode;

    @JsonProperty("STD_FRMLC_NEW_NM")
    private String stdFrmlcNewNm;

    @JsonProperty("STD_MG_NEW_CODE")
    private String stdMgNewCode;

    @JsonProperty("STD_MG_NEW_NM")
    private String stdMgNewNm;

    @JsonProperty("STD_QLITY_NEW_CODE")
    private String stdQlityNewCode;

    @JsonProperty("STD_QLITY_NEW_NM")
    private String stdQlityNewNm;

    @JsonProperty("SBID_PRIC")
    private int sbidPric;

    @JsonProperty("STD_MTC_NEW_CODE")
    private String stdMtcNewCode;

    @JsonProperty("STD_MTC_NEW_NM")
    private String stdMtcNewNm;

    @JsonProperty("CPR_MTC_CODE")
    private String cprMtcCode;

    @JsonProperty("CPR_MTC_NM")
    private String cprMtcNm;

    @JsonProperty("DELNG_QY")
    private int delngQy;
}
