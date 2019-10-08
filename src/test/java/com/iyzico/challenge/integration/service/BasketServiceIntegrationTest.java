package com.iyzico.challenge.integration.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Optional;

import com.iyzico.challenge.configuration.ApplicationConfiguration;
import com.iyzico.challenge.entity.Basket;
import com.iyzico.challenge.entity.Basket.BasketStatus;
import com.iyzico.challenge.entity.Member;
import com.iyzico.challenge.repository.BasketRepository;
import com.iyzico.challenge.repository.MemberRepository;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzico.challenge.service.BasketService;
import com.iyzico.challenge.service.MemberService;
import com.iyzico.challenge.service.PaymentService;
import com.iyzico.challenge.service.ProductService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * BasketServiceIntegrationTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class BasketServiceIntegrationTest {

  @Autowired
  private BasketService basketService;

  @Autowired
  public BasketRepository basketRepository;

  @Autowired
  public MemberRepository memberRepository;

  @Autowired
  public ProductRepository productRepository;

  @Autowired
  public ProductService productService;

  @Autowired
  public MemberService memberService;

  @Autowired
  public PaymentService paymentService;

  @MockBean
  public ApplicationConfiguration applicationConfiguration;

  private Member member;

  private Basket basket;

  @Before
  public void setUp() {
    member = new Member(null, "Test Member", "test@test.com", new HashSet<>());
    this.memberRepository.save(member);
    
    basket = new Basket(null, null, null, BasketStatus.NOT_PAYED);
    this.basketRepository.save(basket);
  }

  @Test
  public void getBasket_should_return_basket() {
    // given
    this.member.getBaskets().add(basket);
    // this.basket.setMember(member);

    this.memberRepository.save(member);

    // when
    Optional<Basket> expectedBasket = this.basketService.getBasket(1L, 1L);

    // then
    assertThat(expectedBasket).isEmpty();
  }

  // @Test
  // public void getBasket_should_return_empty_if_no_basket() {
  //   // given
  //   Basket b1 = new Basket(null, member, new HashSet<>(), BasketStatus.NOT_PAYED);

  //   // when
  //   Optional<Basket> expectedBasket = this.basketService.getBasket(b1.getId(), member.getId());

  //   // then
  //   assertThat(expectedBasket).isEmpty();
  // }
}