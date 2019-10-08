package com.iyzico.challenge.unit.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Optional;

import com.iyzico.challenge.controller.BasketController;
import com.iyzico.challenge.entity.Basket;
import com.iyzico.challenge.entity.Basket.BasketStatus;
import com.iyzico.challenge.entity.Member;
import com.iyzico.challenge.service.BasketService;

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
 * BasketControllerTest
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(BasketController.class)
public class BasketControllerTest {

  @Mock
  private BasketService basketService;

  private BasketController basketController;

  Basket basketWithId;

  Basket basketWithoutId;

  @Before
  public void setUp() {
    basketController = new BasketController();
    basketController.basketService = basketService;
    basketWithId = new Basket(1L, new Member(), new HashSet<>(), BasketStatus.NOT_PAYED);
    basketWithoutId = new Basket(null, new Member(), new HashSet<>(), BasketStatus.NOT_PAYED);
  }

  @Test
  public void getBasket_should_return_OK() throws Exception {
    // given
    Basket basket = basketWithId;
    Mockito.when(this.basketService.getBasket(basket.getId(), 1L)).thenReturn(Optional.of(basket));

    // when
    ResponseEntity<Basket> returnProduct = this.basketController.getBasket(basket.getId(), 1L);

    // then
    assertThat(returnProduct.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void getBasket_should_return_NOT_FOUND() throws Exception {
    // given
    Basket basket = basketWithId;
    Mockito.when(this.basketService.getBasket(basket.getId(), 1L)).thenReturn(Optional.empty());

    // when
    ResponseEntity<Basket> returnProduct = this.basketController.getBasket(basket.getId(), 1L);

    // then
    assertThat(returnProduct.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void createBasket_should_return_OK() throws Exception {
    // given
    Basket basket = basketWithId;
    Mockito.when(this.basketService.createBasket(1L)).thenReturn(Optional.of(basket));

    // when
    ResponseEntity<String> returnProduct = this.basketController.createBasket(1L);

    // then
    assertThat(returnProduct.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void createBasket_should_return_NOT_FOUND() throws Exception {
    // given
    Mockito.when(this.basketService.createBasket(1L)).thenReturn(Optional.empty());

    // when
    ResponseEntity<String> returnProduct = this.basketController.createBasket(1L);

    // then
    assertThat(returnProduct.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void addProductToBasket_should_return_OK() throws Exception {
    // given
    Basket basket = basketWithId;
    Mockito.when(this.basketService.addProductToBasket(1L, 1L)).thenReturn(Optional.of(basket));

    // when
    ResponseEntity<String> returnProduct = this.basketController.addProductToBasket(1L, 1L);

    // then
    assertThat(returnProduct.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void addProductToBasket_should_return_NOT_FOUND() throws Exception {
    // given
    Mockito.when(this.basketService.addProductToBasket(1L, 1L)).thenReturn(Optional.empty());

    // when
    ResponseEntity<String> returnProduct = this.basketController.addProductToBasket(1L, 1L);

    // then
    assertThat(returnProduct.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void deleteProductFromBasket_should_return_OK() throws Exception {
    // given
    Basket basket = basketWithId;
    Mockito.when(this.basketService.deleteProductFromBasket(1L, 1L)).thenReturn(Optional.of(basket));

    // when
    ResponseEntity<String> returnProduct = this.basketController.deleteProductFromBasket(1L, 1L);

    // then
    assertThat(returnProduct.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void deleteProductFromBasket_should_return_NOT_FOUND() throws Exception {
    // given
    Mockito.when(this.basketService.deleteProductFromBasket(1L, 1L)).thenReturn(Optional.empty());

    // when
    ResponseEntity<String> returnProduct = this.basketController.deleteProductFromBasket(1L, 1L);

    // then
    assertThat(returnProduct.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void buyBasket_should_return_OK() throws Exception {
    // given
    Basket basket = basketWithId;
    Mockito.when(this.basketService.buyBasket(1L, 1L)).thenReturn(Optional.of(basket));

    // when
    ResponseEntity<String> returnProduct = this.basketController.buyBasket(1L, 1L);

    // then
    assertThat(returnProduct.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  public void buyBasket_should_return_NOT_FOUND() throws Exception {
    // given
    Mockito.when(this.basketService.buyBasket(1L, 1L)).thenReturn(Optional.empty());

    // when
    ResponseEntity<String> returnProduct = this.basketController.buyBasket(1L, 1L);

    // then
    assertThat(returnProduct.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

}