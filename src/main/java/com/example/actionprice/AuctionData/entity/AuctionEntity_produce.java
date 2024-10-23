package com.example.actionprice.AuctionData.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Table(name = "auctionData_produce")
public class AuctionEntity_produce extends AuctionBaseEntity{
    public AuctionEntity_produce() {}
}
