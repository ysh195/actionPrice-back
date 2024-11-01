package com.example.actionprice.AuctionData.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name="auction_category")

public class AuctionCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long del_id;

    private String large;
    private String middle;
    private String productName;
    private String productRank;
}
