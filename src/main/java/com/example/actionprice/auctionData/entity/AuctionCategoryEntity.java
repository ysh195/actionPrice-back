package com.example.actionprice.auctionData.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author 연상훈
 * @updated 2024-12-25 오전 10:02 [연상훈] : 인덱스 추가.
 * 어차피 수정과 삭제는 할 일이 없고, 일정 주기마다 한 번만 하면 되니까 성능 저하의 우려는 적음.
 * 조회만을 위한 객체인 만큼 조회 속도 향상이 중요
 */
@Entity
@Getter
@Table(
    name = "auction_category",
    indexes = {
        @Index(name = "idx_large", columnList = "large"),
        @Index(name = "idx_large_middle", columnList = "large, middle"),
        @Index(name = "idx_large_middle_productName", columnList = "large, middle, productName"),
        @Index(name = "idx_large_middle_productName_productRank", columnList = "large, middle, productName, productRank")
    }
)
@NoArgsConstructor
@AllArgsConstructor
public class AuctionCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="categoryId")
    private Long categoryId;

    @Column(name = "large")
    private String large;

    @Column(name = "middle")
    private String middle;

    @Column(name = "productName")
    private String productName;

    @Column(name = "productRank")
    private String productRank;
}
