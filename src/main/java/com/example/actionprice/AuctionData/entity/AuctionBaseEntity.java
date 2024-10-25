package com.example.actionprice.AuctionData.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class AuctionBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long del_id;

    private LocalDate del_date; //거래일자

    private String large; //대분류

    private String middle; //중분류

    private String product_name; //상품명

    @Column(nullable = true)
    private String product_rank; // 등급(상품, 중품)

    private int price; // 가격

    private String market_name; //거래장 이름

    @Column(nullable = true)
    private String del_unit; // 단위(중량 : kg 등)


}
