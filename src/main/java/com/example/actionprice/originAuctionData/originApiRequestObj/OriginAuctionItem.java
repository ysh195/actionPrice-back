package com.example.actionprice.originAuctionData.originApiRequestObj;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OriginAuctionItem {

    @JsonProperty("p_product_cls_code")
    private String productClsCode;// 구분 ( 01:소매, 02:도매, default:02 )

    @JsonProperty("p_country_code")
    private String countryCode; // 지역 | default : 전체지역

    @JsonProperty("p_regday")
    private String regDay; // 날짜 yyyy-mm-dd

    @JsonProperty("p_convert_kg_yn")
    private String convertKgYn;  //	kg단위 환산여부

    @JsonProperty("p_category_code")
    private String categoryCode; // 부류코드(100:식량작물, 200:채소류, 300:특용작물, 400:과일류, 500:축산물, 600:수산물, default:100)

    @JsonProperty("p_cert_key")
    private String certKey; // 인증Key

    @JsonProperty("p_cert_id")
    private String certId; // 요청자id

    @JsonProperty("p_returntype")
    private String returnType; // 리턴타입
}
