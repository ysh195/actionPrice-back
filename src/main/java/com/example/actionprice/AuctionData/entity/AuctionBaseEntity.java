package com.example.actionprice.AuctionData.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class AuctionBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long del_id; //거래아이디

    private String del_date; //거래일자
    private String large; //대분류
    private String middle; //중분류
    private String product_name; //상품명
    private String price; // 가격
    private String market_name; //거래장 이름
    private String del_unit; // 단위(중량 : kg 등)


}
