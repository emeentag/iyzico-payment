package com.iyzico.challenge.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Member
 */
@Entity
@Data
@AllArgsConstructor
public class Member {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "name", columnDefinition = "VARCHAR(120)")
  private String name;

  @Column(name = "email", columnDefinition = "VARCHAR(120)")
  private String email;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private Set<Basket> baskets;

  public Member() {
    this.baskets = new HashSet<>();
  }
}