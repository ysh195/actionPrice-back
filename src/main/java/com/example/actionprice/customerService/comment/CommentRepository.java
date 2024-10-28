package com.example.actionprice.customerService.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 연상훈
 * @created 2024-10-27 오후 1:25
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {
  Page<Comment> findByPost_PostId(Integer postId, Pageable pageable);
  Page<Comment> findByUser_Username(String username, Pageable pageable);
}
