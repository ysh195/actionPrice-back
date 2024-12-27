package com.example.actionprice.auctionData.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

// TODO 엑셀 파일로 import 할 때 테이블의 컬럼명 수정
/**
 * 각 DB에 들어갈 엔티티의 베이스 엔티티
 * @author 연상훈
 * @created 2024-11-08 오후 2:46
 * @updated 2024-11-10 오후 9:45 : delDate의 set 메서드 추가. 그래프 객체 때문에 delDate의 set 메서드가 반드시 필요
 * @updated 2024-12-25 오전 10:02 [연상훈] : 상속한 자식 클래스에 인덱스 추가.
 * 어차피 수정과 삭제는 할 일이 없고, 일정 주기마다 한 번만 하면 되니까 성능 저하의 우려는 적음.
 * 조회만을 위한 객체인 만큼 조회 속도 향상이 중요
 * @info MappedSuperclass와 SuperBuilder로 엔티티 변환이 자유롭도록 함
 * @info price는 @Positive를 붙여서 0 또는 양수만 가능
 */
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class AuctionBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="delId")
    private Long delId;

    @Column(name="delDate")
    private LocalDate delDate; //거래일자

    @Column(name = "large")
    private String large; //대분류

    @Column(name = "middle")
    private String middle; //중분류

    @Column(name = "productName")
    private String productName; // 상품명

    @Column(name = "productRank", nullable = true)
    private String productRank; // 등급(상품, 중품)

    @Positive
    @Column(name = "price")
    private int price; // 가격

    @Column(name = "marketName")
    private String market_name; //거래장 이름

    @Column(name = "delUnit", nullable = true)
    private String del_unit; // 단위(중량 : kg 등)

    public void setDelDate(LocalDate delDate) {
        this.delDate = delDate;
    }
}
