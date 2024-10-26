package com.example.actionprice.customerService.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
  Page<Comment> findByPost_PostId(Integer postId, Pageable pageable);
  Page<Comment> findByUser_Username(String username, Pageable pageable);
}
