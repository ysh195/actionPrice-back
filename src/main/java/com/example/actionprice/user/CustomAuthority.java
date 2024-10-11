package com.example.actionprice.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "user_authorities", indexes = {
    @Index(name = "idx_authority_id", columnList = "authority_id")
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomAuthority implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int authority_id;

  @Column(nullable = false, unique = true)
  private String authority;

  @Override
  public String getAuthority() {
    return authority;
  }
}
