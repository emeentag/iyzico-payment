package com.iyzico.challenge.repository;

import java.util.Optional;

import com.iyzico.challenge.entity.Basket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * BasketRepository
 */
@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
    public Optional<Basket> findByIdAndMemberId(Long id, Long memberId);
}