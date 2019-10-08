package com.iyzico.challenge.integration.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.iyzico.challenge.configuration.ApplicationConfiguration;
import com.iyzico.challenge.entity.Member;
import com.iyzico.challenge.middleware.exception.ResourceNotFoundException;
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
    member = new Member(null, "Test Member", "test@test.com");

    this.memberRepository.save(member);
  }

  @Test
  public void addMember_should_save_and_return_saved_member() {
    // given
    Member m = new Member(null, "Test Member", "test@test.com");

    // when
    Member expectedMember = this.memberService.addMember(m);
    Optional<Member> memberInDB = this.memberRepository.findById(2L);

    // then
    assertThat(expectedMember.getId()).isEqualTo(memberInDB.get().getId());
    assertThat(expectedMember.getName()).isEqualTo(memberInDB.get().getName());
    assertThat(expectedMember.getEmail()).isEqualTo(memberInDB.get().getEmail());
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

  @Test(expected = ResourceNotFoundException.class)
  public void updateMember_should_throw_exception_if_not_member_exist() {
    // given
    Member m1 = new Member(200L, "Test Member", "test@test.com");

    // when
    this.memberService.updateMember(m1);

    // then throw exception
  }

  @Test(expected = InvalidDataAccessApiUsageException.class)
  public void updateMember_should_throw_exception_if_no_member_id() {
    // given
    Member m1 = new Member(null, "Test Member", "test@test.com");

    // when
    this.memberService.updateMember(m1);

    // then throw exception
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

  @Test(expected = ResourceNotFoundException.class)
  public void deleteMember_should_not_delete_member_if_not_exist() {
    // given
    this.memberService.memberRepository = Mockito.mock(MemberRepository.class);
    Mockito.when(this.memberService.memberRepository.findById(member.getId())).thenReturn(Optional.empty());

    // when
    this.memberService.deleteMember(member.getId());

    // then throw exception
  }

}