package com.iyzico.challenge.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Basket
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Basket extends Auditable {

  public enum BasketStatus {
    NOT_PAYED, PAYED
  }

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "member_id", referencedColumnName = "id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIgnore
  private Member member;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "basket_product", joinColumns = {
      @JoinColumn(name = "basket_id", referencedColumnName = "id") }, inverseJoinColumns = {
          @JoinColumn(name = "product_id", referencedColumnName = "id") })
  private Set<Product> products = new HashSet<>();

  @Enumerated(EnumType.STRING)
  private BasketStatus status = BasketStatus.NOT_PAYED;
}