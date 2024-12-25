package com.example.actionprice.auctionData.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Table(
    name = "auctionData_vegetable",
    indexes = {
        @Index(name = "idx_large", columnList = "large"),
        @Index(name = "idx_large_middle", columnList = "large, middle"),
        @Index(name = "idx_large_middle_productName", columnList = "large, middle, productName"),
        @Index(name = "idx_large_middle_productName_productRank", columnList = "large, middle, productName, productRank"),
        @Index(name = "idx_large_middle_productName_productRank_delDate", columnList = "large, middle, productName, productRank, delDate")
    }
)
public class AuctionEntity_vegetable extends AuctionBaseEntity{
    public AuctionEntity_vegetable() {}
}
