package com.iyzico.challenge.unit.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.iyzico.challenge.controller.MemberController;
import com.iyzico.challenge.entity.Member;
import com.iyzico.challenge.service.MemberService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * MemberControllerTest
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(MemberController.class)
public class MemberControllerTest {

  @Mock
  private MemberService memberService;

  private MemberController memberController;

  Member memberWithId;

  Member memberWithoutId;

  @Before
  public void setUp() {
    memberController = new MemberController();
    memberController.memberService = memberService;
    memberWithId = new Member(1L, "Test Member", "test@test.com");
    memberWithoutId = new Member(null, "Test Member", "test@test.com");
  }

  @Test
  public void getMember_should_return_OK() throws Exception {
    // given
    Member member = memberWithId;
    Mockito.when(this.memberService.getMember(member.getId())).thenReturn(Optional.of(member));

    // when
    ResponseEntity<Member> returnMember = this.memberController.getMember(member.getId());

    // then
    assertThat(returnMember.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void getMember_should_return_NOT_FOUND() throws Exception {
    // given
    Member member = memberWithId;
    Mockito.when(this.memberService.getMember(member.getId())).thenReturn(Optional.empty());

    // when
    ResponseEntity<Member> returnMember = this.memberController.getMember(member.getId());

    // then
    assertThat(returnMember.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void addMember_should_return_OK() throws Exception {
    // given
    Member member = memberWithId;
    Mockito.when(this.memberService.addMember(member)).thenReturn(member);

    // when
    ResponseEntity<String> returnMember = this.memberController.addMember(member);

    // then
    assertThat(returnMember.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void updateMember_should_return_OK() throws Exception {
    // given
    Member member = memberWithId;
    Mockito.when(this.memberService.updateMember(member)).thenReturn(Optional.of(member));

    // when
    ResponseEntity<String> returnMember = this.memberController.updateMember(member);

    // then
    assertThat(returnMember.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void updateMember_should_return_NOT_FOUND() throws Exception {
    Member member = memberWithId;
    Mockito.when(this.memberService.updateMember(member)).thenReturn(Optional.empty());

    // when
    ResponseEntity<String> returnMember = this.memberController.updateMember(member);

    // then
    assertThat(returnMember.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void deleteMember_should_return_OK() throws Exception {
    // given
    Member member = memberWithId;
    Mockito.when(this.memberService.deleteMember(member.getId())).thenReturn(Optional.of(member));

    // when
    ResponseEntity<String> returnMember = this.memberController.deleteMember(member.getId());

    // then
    assertThat(returnMember.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void deleteMember_should_return_NOT_FOUND() throws Exception {
    // given
    Member member = memberWithId;
    Mockito.when(this.memberService.deleteMember(member.getId())).thenReturn(Optional.empty());

    // when
    ResponseEntity<String> returnMember = this.memberController.deleteMember(member.getId());

    // then
    assertThat(returnMember.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

}