package com.iyzico.challenge.controller;

import java.util.Optional;

import com.iyzico.challenge.entity.Member;
import com.iyzico.challenge.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * MemberController
 */
@RestController
public class MemberController {

  @Autowired
  public MemberService memberService;

  @GetMapping(value = "/member", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Member> getMember(@RequestParam(required = true, name = "id") Long id) {
    ResponseEntity<Member> response;
    Optional<Member> member = this.memberService.getMember(id);

    if (member.isPresent()) {
      response = ResponseEntity.ok(member.get());
    } else {
      response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    return response;
  }

  @PostMapping(value = "/member", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> addMember(@RequestBody Member member) {
    this.memberService.addMember(member);

    return ResponseEntity.ok("Member saved.");
  }

  @PutMapping(value = "/member", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> updateMember(@RequestBody Member member) {
    ResponseEntity<String> response;
    Optional<Member> m = this.memberService.updateMember(member);

    if (m.isPresent()) {
      response = ResponseEntity.ok("Member updated.");
    } else {
      response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    return response;
  }

  @DeleteMapping(value = "/member", produces = MediaType.TEXT_HTML_VALUE)
  public ResponseEntity<String> deleteMember(@RequestParam(required = true, name = "id") Long id) {
    ResponseEntity<String> response;
    Optional<Member> m = this.memberService.deleteMember(id);

    if (m.isPresent()) {
      response = ResponseEntity.ok("Member deleted.");
    } else {
      response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    return response;
  }
}