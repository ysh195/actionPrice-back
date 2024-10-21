package com.example.actionprice.customerService.post;

import org.springframework.data.jpa.domain.Specification;

public class PostSpecifications {

    public static Specification<Post> hasTitle(String title) {
        return (root, query, cb) -> cb.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Post> hasUserName(String keyword) {
        return (root, query, cb) -> cb.like(root.get("username"),"%" + keyword + "%");
    }
}
