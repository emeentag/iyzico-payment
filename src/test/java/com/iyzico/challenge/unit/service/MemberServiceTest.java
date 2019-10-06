package com.iyzico.challenge.unit.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.iyzico.challenge.entity.Member;
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
    memberWithId = new Member(1L, "Test Member", "test@test.com", null);
    memberWithoutId = new Member(null, "Test Member", "test@test.com", null);
  }

  @Test
  public void addMember_should_return_empty_when_has_id() {
    // given
    Member member = memberWithId;

    // when
    Optional<Member> returnMember = memberService.addMember(member);

    // then
    assertThat(returnMember).isEmpty();
  }

  @Test
  public void addMember_should_return_member_when_has_not_id() throws Exception {
    // given
    Member member = memberWithoutId;
    Mockito.when(memberRepository.save(member)).thenReturn(member);

    // when
    Optional<Member> returnMember = memberService.addMember(member);

    // then
    assertThat(returnMember).isEqualTo(Optional.of(member));
  }

  @Test
  public void updateMember_should_return_empty_when_has_not_id() throws Exception {
    // given
    Member member = memberWithoutId;

    // when
    Optional<Member> returnMember = memberService.updateMember(member);

    // then
    assertThat(returnMember).isEmpty();
  }

  @Test
  public void updatemember_should_return_member_when_has_id() throws Exception {
    // given
    Member member = memberWithId;
    Optional<Member> returnMember = Optional.of(member);
    Mockito.when(memberRepository.save(member)).thenReturn(member);
    Mockito.when(memberRepository.findById(member.getId())).thenReturn(returnMember);

    // when
    returnMember = memberService.updateMember(member);

    // then
    assertThat(returnMember).isEqualTo(Optional.of(member));
  }

  @Test
  public void updateMember_should_return_empty_when_is_not_present() throws Exception {
    // given
    Member member = memberWithId;
    Optional<Member> returnMember = Optional.empty();
    Mockito.when(memberRepository.findById(member.getId())).thenReturn(returnMember);

    // when
    returnMember = memberService.updateMember(member);

    // then
    assertThat(returnMember).isEmpty();
  }

  @Test
  public void getMember_should_return_member() throws Exception {
    // given
    Member member = memberWithId;
    Mockito.when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

    // when
    Optional<Member> returnMember = memberService.getMember(member.getId());

    // then
    assertThat(returnMember).isEqualTo(Optional.of(member));
  }

  @Test
  public void deleteMember_should_return_deleted_when_there_is_sth_do_delete() throws Exception {
    // given
    Member member = memberWithId;
    Mockito.when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

    // when
    String returnString = memberService.deleteMember(member.getId());

    // then
    assertThat(returnString).isEqualTo("Deleted.");
  }

  @Test
  public void deleteMember_should_return_not_deleted_when_there_is_sth_do_delete() throws Exception {
    // given
    Member member = memberWithId;
    Mockito.when(memberRepository.findById(member.getId())).thenReturn(Optional.empty());

    // when
    String returnString = memberService.deleteMember(member.getId());

    // then
    assertThat(returnString).isEqualTo("No member to delete.");
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