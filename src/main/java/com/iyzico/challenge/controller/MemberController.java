package com.iyzico.challenge.controller;

import com.iyzico.challenge.entity.Member;

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

  @GetMapping(value = "/member", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Member> getMember(@RequestParam(required = true, name = "id") Long id) {
    return null;
  }

  @PostMapping(value = "/member", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> addMember(@RequestBody Member member) {
    return null;
  }

  @PutMapping(value = "/member", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> updateMember(@RequestBody Member member) {
    return null;
  }

  @DeleteMapping(value = "/member")
  public ResponseEntity<String> deleteMember(@RequestParam(required = true, name = "id") Long id) {
    return null;
  }
}