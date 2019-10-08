package com.iyzico.challenge.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Member
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Member extends Auditable {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "name", columnDefinition = "VARCHAR(120)")
  private String name;

  @Column(name = "email", columnDefinition = "VARCHAR(120)")
  private String email;
}