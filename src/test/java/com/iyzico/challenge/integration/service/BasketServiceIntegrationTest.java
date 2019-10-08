package com.iyzico.challenge.integration.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import com.iyzico.challenge.configuration.ApplicationConfiguration;
import com.iyzico.challenge.entity.Basket;
import com.iyzico.challenge.entity.Basket.BasketStatus;
import com.iyzico.challenge.middleware.exception.ResourceNotFoundException;
import com.iyzico.challenge.entity.Member;
import com.iyzico.challenge.entity.Product;
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
    member = new Member(null, "Test Member", "test@test.com");
    this.memberRepository.saveAndFlush(member);

    basket = new Basket(null, member, null, BasketStatus.NOT_PAYED);
    this.basketRepository.saveAndFlush(basket);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void getBasket_should_thorow_exception_if_no_member() {
    // when
    this.basketService.getBasket(1L, 100L);

    // then throw exception
  }

  @Test(expected = ResourceNotFoundException.class)
  public void getBasket_should_thorow_exception_if_no_basket() {
    // when
    this.basketService.getBasket(100L, 1L);

    // then throw exception
  }

  @Test
  public void getBasket_should_return_basket() {
    // when
    Optional<Basket> b =this.basketService.getBasket(1L, 1L);

    // then
    assertThat(b.get().getId()).isEqualTo(basket.getId());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void createBasket_should_thorow_exception_if_no_member() {
    // when
    this.basketService.createBasket(100L);

    // then throw exception
  }

  @Test
  public void createBasket_should_create_a_basket_for_member() {
    // when
    Optional<Basket> b = this.basketService.createBasket(1L);

    // then throw exception
    assertThat(b.get().getMember().getEmail()).isEqualTo(member.getEmail());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void addProductToBasket_should_thorow_exception_if_no_basket() {
    // when
    this.basketService.addProductToBasket(100L, 1L);

    // then throw exception
  }

  @Test(expected = ResourceNotFoundException.class)
  public void addProductToBasket_should_thorow_exception_if_no_product() {
    // when
    this.basketService.addProductToBasket(1L, 100L);

    // then throw exception
  }

  @Test
  public void addProductToBasket_should_add_a_ptoduct_for_basket() {
    // given
    Product p = new Product();
    p.setName("Test product");
    p.setPrice(new BigDecimal("10"));
    p.setStockCount(10L);
    this.productRepository.save(p);

    // when
    Optional<Basket> b = this.basketService.addProductToBasket(1L, 1L);

    // then throw exception
    assertThat(p.getBaskets().size()).isEqualTo(1);
  }
}