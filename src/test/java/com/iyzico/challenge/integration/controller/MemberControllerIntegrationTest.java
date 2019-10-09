package com.iyzico.challenge.integration.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.entity.Member;
import com.iyzico.challenge.repository.MemberRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * MemberControllerIntegrationTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class MemberControllerIntegrationTest {

  @Autowired
  public MemberRepository memberRepository;

  @Autowired
  public MockMvc mockMvc;

  private Member member;

  private ObjectMapper objectMapper;

  @Before
  public void setUp() {
    member = new Member(null, "Test member", "test@test.com");

    this.memberRepository.save(member);

    objectMapper = new ObjectMapper();
  }

  @Test
  public void addMember_should_return_OK() throws IllegalArgumentException, Exception {
    // given
    Member m = new Member(null, "Test member 2", "test@test.com");

    // when
    mockMvc.perform(MockMvcRequestBuilders.post("/member").contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(m))).andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  public void getMember_should_return_OK_and_right_content() throws IllegalArgumentException, Exception {
    // given
    Member m = new Member(null, "Test member 2", "test2@test.com");
    this.memberRepository.save(m);

    // when
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/member?id=1"))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    // then
    String content = result.getResponse().getContentAsString();
    assertThat(content.contains("\"id\":1")).isTrue();
  }

  @Test
  public void getMember_should_return_NOT_FOUND() throws IllegalArgumentException, Exception {
    // given
    Member m = new Member(null, "Test member 2", "test2@test.com");
    this.memberRepository.save(m);

    // when
    mockMvc.perform(MockMvcRequestBuilders.get("/member?id=100")).andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn();
  }

  @Test
  public void updateMember_should_return_OK() throws IllegalArgumentException, Exception {
    // given
    Member m = new Member(1L, "Test member 2", "test2@test.com");

    // when
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/member")
        .contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(m)))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    String content = result.getResponse().getContentAsString();

    // then
    assertThat(content.contains("Member updated.")).isTrue();
  }

  @Test
  public void updateMember_should_return_NOT_FOUND() throws IllegalArgumentException, Exception {
    // given
    Member m = new Member(100L, "Test member 2", "test2@test.com");

    // when
    mockMvc.perform(MockMvcRequestBuilders.put("/member").contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(m))).andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void deleteMember_should_return_OK() throws IllegalArgumentException, Exception {
    // when
    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/member?id=1"))
        .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    String content = result.getResponse().getContentAsString();

    // then
    assertThat(content.contains("Member deleted.")).isTrue();
  }

  @Test
  public void deleteMember_should_return_NOT_FOUND() throws IllegalArgumentException, Exception {
    // when
    mockMvc.perform(MockMvcRequestBuilders.delete("/member?id=100"))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

}