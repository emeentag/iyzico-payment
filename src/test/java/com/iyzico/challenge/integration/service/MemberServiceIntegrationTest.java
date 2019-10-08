package com.iyzico.challenge.integration.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Optional;

import com.iyzico.challenge.configuration.ApplicationConfiguration;
import com.iyzico.challenge.entity.Member;
import com.iyzico.challenge.repository.MemberRepository;
import com.iyzico.challenge.service.MemberService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * MemberServiceIntegrationTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class MemberServiceIntegrationTest {

  @Autowired
  public MemberService memberService;

  @Autowired
  public MemberRepository memberRepository;

  @MockBean
  public ApplicationConfiguration applicationConfiguration;

  private Member member;

  @Before
  public void setUp() {
    member = new Member(null, "Test Member", "test@test.com", new HashSet<>());

    this.memberRepository.save(member);
  }

  @Test
  public void addMember_should_save_if_not_in_db() {
    // given
    Member m = new Member(null, "Test Member", "test@test.com", new HashSet<>());

    // when
    Optional<Member> expectedMember = this.memberService.addMember(m);
    Optional<Member> memberInDB = this.memberRepository.findById(2L);

    // then
    assertThat(expectedMember.get().getId()).isEqualTo(memberInDB.get().getId());
    assertThat(expectedMember.get().getName()).isEqualTo(memberInDB.get().getName());
    assertThat(expectedMember.get().getEmail()).isEqualTo(memberInDB.get().getEmail());
  }

  @Test
  public void addMember_should_not_save_if_exist_in_db() {
    // when
    Optional<Member> expectedMember = this.memberService.addMember(member);

    // then
    assertThat(expectedMember).isEmpty();
  }

  @Test
  public void getMember_should_return_member() {
    // when
    Optional<Member> expectedMember = this.memberService.getMember(member.getId());

    // then
    assertThat(expectedMember.get().getId()).isEqualTo(member.getId());
  }

  @Test
  public void updateMember_should_update_member() {
    // given
    member.setEmail("update@test.com");

    // when
    Optional<Member> expectedMember = this.memberService.updateMember(member);

    // then
    assertThat(expectedMember.get().getEmail()).isEqualTo(member.getEmail());
  }

  @Test(expected = InvalidDataAccessApiUsageException.class)
  public void updateMember_should_not_update_member_if_not_exist() {
    // given
    Member m1 = new Member(null, "Test Member", "test@test.com", new HashSet<>());

    // when
   this.memberService.updateMember(m1);
  }

  @Test
  public void deleteMember_should_delete_member() {
    // given
    this.memberService.memberRepository = Mockito.mock(MemberRepository.class);
    Mockito.when(this.memberService.memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

    // when
    this.memberService.deleteMember(member.getId());

    // then
    Mockito.verify(this.memberService.memberRepository, Mockito.atLeast(1)).delete(member);
  }

  @Test
  public void deleteMember_should_not_delete_member_if_not_exist() {
    // given
    this.memberService.memberRepository = Mockito.mock(MemberRepository.class);
    Mockito.when(this.memberService.memberRepository.findById(member.getId())).thenReturn(Optional.empty());

    // when
    this.memberService.deleteMember(member.getId());

    // then
    Mockito.verify(this.memberService.memberRepository, Mockito.never()).delete(member);
  }

}