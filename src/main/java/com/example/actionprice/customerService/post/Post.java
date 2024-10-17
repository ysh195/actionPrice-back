package com.example.actionprice.customerService.post;

import com.example.actionprice.common.BaseEntity;
import com.example.actionprice.user.User;
import com.example.actionprice.customerService.comment.Comment;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

/**
 * @author 연상훈
 * @created 2024-10-05 오후 10:45
 * @value postId
 * @value title
 * @value content : columnDefinition = "TEXT 설정을 사용하여 줄 나눔 가능한 긴 문자열로 설정합니다.
 * @value published : 비밀글 설정입니다. [default = true(공개)]
 * @value user
 * @value commentSet
 * @info 1. content는 1자 이상이며, 글자수 무제한, 줄 나눔 가능한 텍스트입니다.
 * @info 2. @JsonManagedReference, @JsonBackReference와 @ToString(exclude = {"user", "commentSet"})로 순환참조의 위험을 경감시켰습니다.
 * @info 3. createdAt, updatedAt는 BaseEntity를 통해 구현하였습니다.
 * @info 4. published로 비공개글을 구현합니다. true = 공개 / false = 비공개
 * @info 5. 이걸 참조하는 쪽에서 OneToMany로 불러올 때 순번대로 정렬하기 위해 Comparable을 implements 함
 */
@Entity
@Table(name = "post")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"commentSet"})
public class Post extends BaseEntity implements Comparable<Post> {

  // field

  // field - basic
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "postId")
  private int postId;

  @Column(nullable=false)
  @Size(min = 1, max = 30)
  private String title;

  @Column(nullable=false, columnDefinition = "TEXT")
  @Size(min = 1)
  private String content;

  @Column
  @Builder.Default
  private boolean published = true;

  // LocalDateTime createdAt | from BaseEntity | auto generation
  // LocalDateTime updatedAt | from BaseEntity | auto generation

  // field - relationship
  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "username", nullable = false)
  private User user;

  @JsonManagedReference
  @OneToMany(mappedBy = "post",
      orphanRemoval = true,
      cascade = {CascadeType.ALL},
      fetch = FetchType.LAZY)
  @BatchSize(size = 10)
  @Builder.Default
  private Set<Comment> commentSet = new HashSet<>();

  // method
  @Override
  public int compareTo(Post o) {
    return this.postId - o.postId;
  }

  public void addComment(Comment comment) {
    if(this.commentSet.stream().noneMatch(c -> c.getCommentId() == comment.getCommentId())){
      this.commentSet.add(comment);
    }
  }

  /**
   * @author : 연상훈
   * @created : 2024-10-06 오후 12:17
   * @updated : 2024-10-06 오후 12:17
   * @see : comment는 user와 post를 모두 부모로 가지기 때문에 제거할 때 더 조심해야 함. comment 객체의 삭제 과정 설명
   * 1. 부모 객체에서 지움
   * 2. orphanRemoval = true, cascade = {CascadeType.ALL}로, 부모와의 연결 끊어지면 지워짐
   * 3. 근데 지워진다는 신호를 받으면 지워지기 전에 @EntityListeners(CommentListener.class) 내부의 comment 객체 제거 로직이 발동
   * 4. 양쪽 부모에서 안전하게 지워짐.
   * 5. if-contains로 간단하게 하려면 commentSet에 equals를 오버라이드 해야 하는데, 거기에 엮인 게 많아서 생각보다 귀찮음
   */
  public void removeComment(Comment comment) {
    this.commentSet.removeIf(c -> c.getCommentId() == comment.getCommentId());
  }
}
