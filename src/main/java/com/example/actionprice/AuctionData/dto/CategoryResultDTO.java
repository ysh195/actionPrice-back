package com.example.actionprice.AuctionData.dto;

import com.example.actionprice.customerService.comment.Comment;
import com.example.actionprice.customerService.post.Post;
import com.example.actionprice.customerService.post.dto.PostSimpleDTO;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Getter
@Builder
public class CategoryResultDTO {

    private String large;
    private String middle;
    private String small;
    private String rank;
    private int averagePrice;
    private LocalDate startDate;
    private LocalDate endDate;



}
