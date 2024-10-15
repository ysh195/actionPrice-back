package com.example.actionprice.newAuctionData;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "newAuction_data")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class newAuctionEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rn;

    private String saleDate; // 정산일자

    private String whsalCd; // 도매시장코드

    private String large; // 대분류코드

    private String mid; // 중분류코드

    private String small; // 소분류코드

    private String pumName; // 품목명

    private int danq; // 단위 중량

    private String sizeCd; // 크기 코드

    private String lvCd; // 등급 코드

    private int qty; // 물량

    private String cost; // 단가

}
