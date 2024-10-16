package com.example.actionprice.AuctionData.entity;

import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

/**
 * @author 연상훈
 * @created 2024-10-16 오후 3:13
 * @updated 2024-10-16 오후 3:13
 * @value : del_date = 거래 일자 [구 - DELNG_DE / 신 - saleDate]
 * @value : large = 대분류 [구 - (...).getLarge() / 신 - (...).getLarge()]
 * @value : middle = 중분류 [구 - (...).getMiddle() / 신 - (...).getMiddle()]
 * @value : small = 소분류 [구 - (...).getSmall() / 신 - (...).getSmall()]
 * @value : price = 가격 [구 - SBID_PRIC / 신 - cost ]
 * @value : del_unit = 단위(중량 : kg 등) [구 - STD_UNIT_NEW_CODE / 신 - danCd ]
 * @value : quantity = 거래된 갯수(양) [ 구 - DELNG_QY / 신 - qty ]
 * @value : size = 사이즈 [구 - STD_MG_NEW_CODE / 신 - sizeCd ]
 * @value : level = 등급 [구 - STD_QLITY_NEW_CODE / 신 - lvCd ]
 * @see : {large, middle. small} [구 - detailCategoryRepository.findById(row.getSTD_SPCIES_NEW_CODE()) / 신 - detailCategoryRepository.findById((row.getLarge()+row.getMiddle()+row.getSmall()))]
 */
@SuperBuilder
@Entity
@Table(name = "ani_data")
public class AuctionEntity_ani extends AuctionDataBaseEntity{
    public AuctionEntity_ani() {}
//  아래의 객체들은 AuctionDataBaseEntity로부터 상속된 것.
//  이러한 것들을 포함하고 있다는 의미로 주석처리 해서 남겨둔 것이니 활성화시키지 말 것.
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long del_id; // 거래 아이디
//    private LocalDate del_date; // 거래 일자
//    private String large; // 대분류
//    private String middle; // 중분류
//    private String small; // 소분류
//    private String product_name; // 상품명
//    private String market_name; // 거래장 이름
//    private int price; // 가격
//    private String del_unit; // 단위(중량 : kg 등)
//    private int quantity; // 거래된 갯수(양)
//    private String size; // 크기
//    private String level; // 등급
}
