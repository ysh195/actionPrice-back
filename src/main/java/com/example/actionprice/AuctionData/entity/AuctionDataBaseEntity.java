package com.example.actionprice.AuctionData.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * 메인 데이터 엔티티를 구성하는 base entity
 * @author 연상훈
 * @created 2024-10-17 오후 7:58
 * @info
 */
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class AuctionDataBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long del_id; // 거래 아이디
    private LocalDate del_date; // 거래 일자
    private String large; // 대분류
    private String middle; // 중분류
    private String small; // 소분류
    private String product_name; // 상품명
    private String market_name; // 거래장 이름
    private int price; // 가격
    private String del_unit; // 단위(중량 : kg 등)
    private int quantity; // 거래된 갯수(양)
    private String size; // 크기
    private String level; // 등급
}
