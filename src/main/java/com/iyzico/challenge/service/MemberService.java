package com.iyzico.challenge.service;

import java.util.Optional;

import com.iyzico.challenge.entity.Member;
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

  public Optional<Member> addMember(Member member) {
    Optional<Member> returnMember = Optional.empty();

    if (member.getId() == null) {
      returnMember = Optional.of(this.memberRepository.save(member));
    }

    return returnMember;
  }

  public Optional<Member> getMember(Long memberId) {

    Optional<Member> returnMember = this.memberRepository.findById(memberId);

    return returnMember;
  }

  public Optional<Member> updateMember(Member member) {

    Long memberId = member.getId();
    Optional<Member> returnMember = Optional.empty();

    if (memberId != null) {
      returnMember = this.memberRepository.findById(memberId);

      if (returnMember.isPresent()) {
        this.memberRepository.save(member);
      }
    }

    return returnMember;
  }

  public String deleteMember(Long memberId) {

    String returnString;

    Optional<Member> returnMember = this.memberRepository.findById(memberId);

    if (returnMember.isPresent()) {
      this.memberRepository.delete(returnMember.get());
      returnString = "Deleted.";
    } else {
      returnString = "No member to delete.";
    }

    return returnString;
  }

  public Buyer toBuyer(Member member) {
    Buyer buyer = new Buyer();

    buyer.setId("SSM" + member.getId());
    buyer.setName(member.getName());
    buyer.setEmail(member.getEmail());

    return buyer;
  }
}