package com.example.actionprice.auctionData.dto;

import lombok.Getter;
import lombok.ToString;

/**
 * 각 카테고리 정보를 담는 dto
 * @author 연상훈
 * @created 2024-11-26 오후 4:03
 * @info 하나의 카테고리 테이블에 존재하는 데이터를 사용하기 때문에 중복값이 많음.
 * @info 또 데이터 조회 시 list로 반환하기 때문에, 그것을 distinct()로 간단하게 걸러 내기 위해 equals와 hashcode를 오버라이드함
 * @example
 * 대분류 중분류 소분류 상품등급
 * A    A-1    A-1-1 A-1-1-상
 * A    A-1    A-1-1 A-1-1-중
 * A    A-1    A-1-1 A-1-1-하
 * A    A-1    A-1-2 A-1-2-상
 * ....
 */
@Getter
@ToString
public class CategoryItemDTO {

    private int id; // 고유 ID
    private String name; // 이름

    public CategoryItemDTO(String name) {
        this.name = name;
        this.id = name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.id == ((CategoryItemDTO) obj).id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
