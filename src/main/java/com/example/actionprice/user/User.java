package com.example.actionprice.user;

import com.example.actionprice.customerService.comment.Comment;
import com.example.actionprice.user.favorite.Favorite;
import com.example.actionprice.customerService.post.Post;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
 * @author : 연상훈
 * @created : 2024-10-05 오후 10:04
 * @updated : 2024-10-06 오후 12:29
 * 2024-10-06 오후 12:29 - 간편한 권한 관리를 위해 Set<String> authorities 사용
 * @info :
 * 1. 순환참조의 문제를 피하기 위해 @ToString(exclude={})와 @JsonManagedReference를 사용했습니다.
 * 2. 원활한 하위 객체 관리를 위해 orphanRemoval = true과 cascade = {CascadeType.ALL}를 사용했습니다.
 * 3. 객체 호출 시의 효율을 위해 fetch = FetchType.LAZY와 @BatchSize(size)를 사용했습니다.
 * 4. 이메일은 회원가입 시 필수이기 때문에 unique이며, not null입니다.
 * 5. 즐겨찾기가 과도하게 늘어나는 것을 막기 위해 10개로 제한했습니다.
 * 6. OneToMany 관계에 있는 객체의 컬렉션 타입은, 해당 객체를 쉽게 찾아낼 수 있도록 set으로 설정했습니다.
 * 7. jwt 토큰은 로그인 시에 발급하는 것이지, 계정 생성 시에 발급하는 것이 아니기 때문에 생성할 당시에는 발급되지 않습니다. 그렇기 때문에 nullable입니다.
 * 8.추후 수정 및 추가가 예고된 항목들의 수정 및 추가를 위해 Builder를 사용하는 것은 불필요한 코드의 증가를 야기하므로, set 메서드를 지정하여 단순화합니다.
 * 9. set 객체들에 이미 존재하는 것을 추가하거나 존재하지 않는 것을 삭제할 때의 에러처리를 하느니, 차라리 에러 없이 안정적으로 실행되는 것이 효율적일 것 같아서 add/remove 메서드를 좀 더 복잡하게 구성함
 */
@Entity
@Table(name="user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"postSet", "commentSet", "favoriteSet"}) //exclude : 제외시킨다
public class User {

  // field

  // field - basic
  @Id
  @Column(name = "username")
  @Size(min = 6, max=20)
  private String username;

  @Column(nullable=false, name = "password")
  private String password;

  @Column(nullable=false, unique=true)
  @Email
  private String email;

  @Column(nullable=true) //유저 생성당시는 null일 수 밖에 없음 로그인시 생성되기에
  private String refreshToken; //블랙리스트를 사용

  /**
   * 유저 권한
   * @author : 연상훈
   * @created : 2024-10-11 오후 11:33
   * @updated : 2024-10-11 오후 11:33
   * @info : 권한 관리를 간편하게 하기 위해 Set<String>으로 변경
   */
  @ElementCollection(fetch = FetchType.EAGER)
  @Builder.Default
  private Set<String> authorities = new HashSet<>(); // 권한 객체를 별도로 관리해서 유저 생성할 때마다 권한이 쓸데없이 늘어나는 것을 방지

  // field - relationship
  @JsonManagedReference //부모객체에서 자식객체 관리 json형태로 반환될때 이게 부모라는것을 알려줌
  @OneToMany(mappedBy = "user",
      orphanRemoval = true, //유저에게서 나가면 삭제가 됨
      cascade = {CascadeType.ALL}, //유저객체에서 포스트를 불러옴 ,포스트 내용을 수정 그것을 세이브 (전체반영)
      fetch = FetchType.LAZY)
  @BatchSize(size = 10) //한번에 불러오는 양
  @Builder.Default //만들때 빌더를 사용해 자신의 필드를 다 설정해야하지만 안하면 null 이기에 dafault는 new HashSet<>() 이다
  private Set<Post> postSet = new HashSet<>(); //게시글 (유저 참조중)

  @JsonManagedReference
  @OneToMany(mappedBy = "user",
      orphanRemoval = true,
      cascade = {CascadeType.ALL}, //변환할떄
      fetch = FetchType.LAZY) //유저를 불러올떄 포스트는 나중에 불러오게 함
  @BatchSize(size = 10)
  @Builder.Default

  private Set<Comment> commentSet = new HashSet<>(); //댓글

  @JsonManagedReference
  @OneToMany(mappedBy = "user",
      orphanRemoval = true,
      cascade = {CascadeType.ALL},
      fetch = FetchType.LAZY)
  @BatchSize(size = 10)
  @Size(max = 10)
  @Builder.Default
  private Set<Favorite> favoriteSet = new HashSet<>();

  // method
  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public void setPassword(@Size(min = 8, max = 16) String password) {
    this.password = password;
  }

  /**
   * @author : 연상훈
   * @created : 2024-10-06 오전 11:09
   * @updated : 2024-10-11 오후 11:36
   * @param : 오탈자 방지를 위해 UserRole 활용할 것. ex) UserRole.ROLE_USER, UserRole.ROLE_ADMIN.
   * @see : 절대 UserRole.ROLE_USER.name()까지 하면 안 됨. 그건 메서드 내부에서 알아서 함.
   */
  public void addAuthorities(UserRole role) {
    // 이렇게 쓰게함 UserRole.ROLE_USER
    // 없으면 추가
    //authorities 셋 스트림가능 ,noneMatch 이용해 입력받는것의 조건을 보고 다 안맞으면 true
    if (this.authorities.stream().noneMatch(authority -> authority.equals(role.name()))) {
      this.authorities.add(role.name()); //없으면 추가
    }
  }

  /**
   * @author : 연상훈
   * @created : 2024-10-06 오전 11:11
   * @updated : 2024-10-11 오후 11:36
   * @param : 오탈자 방지를 위해 UserRole 활용할 것. ex) UserRole.ROLE_USER, UserRole.ROLE_ADMIN.
   * @see : 절대 UserRole.ROLE_USER.name()까지 하면 안 됨. 그건 메서드 내부에서 알아서 함.
   */
  public void removeAuthorities(UserRole role) {
    // 있으면 삭제
    this.authorities.removeIf(authority -> authority.equals(role.name()));
  }

  public void addPost(Post post) {
    // 없으면 추가
    if(this.postSet.stream().noneMatch(p -> p.getPostId() == post.getPostId())){
      this.postSet.add(post);
    }
  }

  public void removePost(Post post) {
    // 있으면 삭제
    this.postSet.removeIf(p -> p.getPostId() == post.getPostId());
  }

  public void addComment(Comment comment) {
    // 없으면 추가
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
    // 있으면 삭제
    this.commentSet.removeIf(c -> c.getCommentId() == comment.getCommentId());
  }

  public void addFavorite(Favorite favorite) {
    // 없으면 추가
    if (this.favoriteSet.stream().noneMatch(f -> f.getFavoriteId() == favorite.getFavoriteId())){
      this.favoriteSet.add(favorite);
    }
  }

  public void removeFavorite(Favorite favorite) {
    // 있으면 삭제
    this.commentSet.removeIf(c -> c.getCommentId() == favorite.getFavoriteId());
  }
}
