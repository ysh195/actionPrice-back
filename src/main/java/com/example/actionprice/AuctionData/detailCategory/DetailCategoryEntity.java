package com.example.actionprice.AuctionData.detailCategory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품의 대/중/소 분류 카테고리 객체
 * @author 연상훈
 * @created 2024-10-17 오후 7:57
 * @value code
 * @value large
 * @value middle
 * @value small
 */
@Entity
@Table(name = "detail_category")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DetailCategoryEntity {

    @Id
    @Column(name = "detail_category_code", unique = true, nullable = false)
    private String code;

    @Column(name = "detail_category_large", nullable = false)
    private String large;

    @Column(name = "detail_category_middle", nullable = false)
    private String middle;

    @Column(name = "detail_category_small;", nullable = false)
    private String small;

}
