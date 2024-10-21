package com.example.actionprice.customerService.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostRepository extends JpaRepository<Post, Integer>{

    //Containing 키워드는 SQL에서 LIKE '%keyword%'와 동일하게 동작하여 부분 검색
    Page<Post> findByTitleContaining(String keyword, Pageable pageable);
}
