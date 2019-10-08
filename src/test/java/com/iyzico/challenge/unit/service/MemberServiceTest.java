package com.iyzico.challenge.unit.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.iyzico.challenge.entity.Member;
import com.iyzico.challenge.middleware.exception.ResourceNotFoundException;
import com.iyzico.challenge.repository.MemberRepository;
import com.iyzico.challenge.service.MemberService;
import com.iyzipay.model.Buyer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * MemberServiceTest
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MemberService.class)
public class MemberServiceTest {

  @Mock
  private MemberRepository memberRepository;

  private MemberService memberService;

  Member memberWithId;

  Member memberWithoutId;

  @Before
  public void setUp() {
    memberService = new MemberService();
    memberService.memberRepository = memberRepository;
    memberWithId = new Member(1L, "Test Member", "test@test.com");
    memberWithoutId = new Member(null, "Test Member", "test@test.com");
  }

  @Test
  public void addMember_should_return_member_when_has_not_id() throws Exception {
    // given
    Member member = memberWithoutId;
    Mockito.when(memberRepository.save(member)).thenReturn(member);

    // when
    Member memberInDB = this.memberService.addMember(member);

    // then
    assertThat(memberInDB).isEqualTo(member);
  }

  @Test
  public void updateMember_should_return_member_when_has_id() throws Exception {
    // given
    Member member = memberWithId;
    member.setEmail("updated@test.com");
    Member memberInDB = new Member(1L, "Test Member", "test@test.com");
    Optional<Member> returnMember = Optional.of(memberInDB);
    Mockito.when(this.memberRepository.save(member)).thenReturn(member);
    Mockito.when(this.memberRepository.findById(member.getId())).thenReturn(returnMember);

    // when
    returnMember = this.memberService.updateMember(member);

    // then
    assertThat(returnMember.get().getEmail()).isEqualTo(member.getEmail());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void updateMember_should_throw_exception_when_not_found_on_db() throws Exception {
    // given
    Member member = memberWithId;
    Optional<Member> returnMember = Optional.empty();
    Mockito.when(memberRepository.findById(member.getId())).thenReturn(returnMember);

    // when
    returnMember = this.memberService.updateMember(member);

    // then throw exception
  }

  @Test
  public void getMember_should_return_member() throws Exception {
    // given
    Member member = memberWithId;
    Mockito.when(this.memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

    // when
    Optional<Member> returnMember = this.memberService.getMember(member.getId());

    // then
    assertThat(returnMember).isEqualTo(Optional.of(member));
  }

  @Test
  public void deleteMember_should_return_member_when_there_is_sth_to_delete() throws Exception {
    // given
    Member member = memberWithId;
    Mockito.when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

    // when
    Optional<Member> returnMember = memberService.deleteMember(member.getId());

    // then
    assertThat(returnMember).isEqualTo(returnMember);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void deleteMember_should_throw_exception_when_there_is_nothing_delete() throws Exception {
    // given
    Member member = memberWithId;
    Mockito.when(this.memberRepository.findById(member.getId())).thenReturn(Optional.empty());

    // when
    this.memberService.deleteMember(member.getId());

    // then throw exception
  }

  @Test
  public void toBuyer_should_return_a_buyer_item() throws Exception {
    // given
    Member member = memberWithId;

    // when
    Buyer returnItem = memberService.toBuyer(member);

    // then
    assertThat(returnItem.getId()).isEqualTo("SSM" + member.getId());
    assertThat(returnItem.getName()).isEqualTo(member.getName());
    assertThat(returnItem.getEmail()).isEqualTo(member.getEmail());
  }

}