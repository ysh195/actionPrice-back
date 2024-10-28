package com.example.actionprice.AuctionData.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.experimental.SuperBuilder;
import org.springframework.stereotype.Repository;


@Repository
@SuperBuilder
@Entity
@Table(name = "auctionData_fish")
public class AuctionEntity_fish extends AuctionBaseEntity {
    public AuctionEntity_fish() {}
}
