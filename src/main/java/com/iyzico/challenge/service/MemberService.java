package com.iyzico.challenge.service;

import java.util.Optional;

import com.iyzico.challenge.entity.Member;
import com.iyzico.challenge.middleware.exception.ResourceNotFoundException;
import com.iyzico.challenge.repository.MemberRepository;
import com.iyzipay.model.Buyer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * MemberService
 */
@Service
public class MemberService {

  @Autowired
  public MemberRepository memberRepository;

  public Member addMember(Member member) {
    return this.memberRepository.save(member);
  }

  public Optional<Member> getMember(Long memberId) {

    Optional<Member> memberInDB = this.memberRepository.findById(memberId);

    return memberInDB;
  }

  public Optional<Member> updateMember(Member member) {
    final Long memberId = member.getId();

    Optional<Member> memberInDB = this.memberRepository.findById(member.getId());

    memberInDB = memberInDB.map(m -> Optional.of(this.memberRepository.save(member)))
        .orElseThrow(() -> new ResourceNotFoundException("Member: " + memberId + " not found."));

    return memberInDB;
  }

  public Optional<Member> deleteMember(Long memberId) {

    Optional<Member> memberInDB = this.memberRepository.findById(memberId);

    memberInDB = memberInDB.map(m -> {
      this.memberRepository.delete(m);
      return Optional.of(m);
    }).orElseThrow(() -> new ResourceNotFoundException("Member: " + memberId + " not found."));

    return memberInDB;
  }

  public Buyer toBuyer(Member member) {
    Buyer buyer = new Buyer();

    buyer.setId("SSM" + member.getId());
    buyer.setName(member.getName());
    buyer.setSurname(member.getName());
    buyer.setEmail(member.getEmail());
    buyer.setIdentityNumber("74300864791");
    buyer.setLastLoginDate("2015-10-05 12:43:35");
    buyer.setRegistrationDate("2013-04-21 15:12:09");
    buyer.setRegistrationAddress("Nidakule Göztepe, Merdivenköy Mah. Bora Sok. No:1");
    buyer.setIp("85.34.78.112");
    buyer.setCity("Istanbul");
    buyer.setCountry("Turkey");
    buyer.setZipCode("34732");

    return buyer;
  }
}