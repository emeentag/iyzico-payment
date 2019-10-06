package com.iyzico.challenge.repository;

import com.iyzico.challenge.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MemberRepository
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

}