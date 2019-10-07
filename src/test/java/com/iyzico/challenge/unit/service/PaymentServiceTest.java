package com.iyzico.challenge.unit.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.HashSet;

import com.iyzico.challenge.configuration.ApplicationConfiguration;
import com.iyzico.challenge.entity.Basket;
import com.iyzico.challenge.entity.Basket.BasketStatus;
import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.service.MemberService;
import com.iyzico.challenge.service.PaymentService;
import com.iyzico.challenge.service.ProductService;
import com.iyzipay.model.Payment;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * PaymentServiceTest
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PaymentService.class)
public class PaymentServiceTest {

  private PaymentService paymentService;

  @Mock
  public MemberService memberService;

  @Mock
  public ProductService productService;

  @Mock
  public ApplicationConfiguration applicationConfiguration;

  @Before
  public void setUp() {
    paymentService = new PaymentService();
    paymentService.productService = this.productService;
    paymentService.memberService = this.memberService;
    paymentService.applicationConfiguration = this.applicationConfiguration;
  }

  // This test unfortunately not performed because of this error:
  // Cannot cast sun.net.www.protocol.https.HttpsURLConnectionImpl to
  // javax.net.ssl.HttpsURLConnection
  // Seems like unit test context is generating HttpsURLConnection from sun.net in
  // com.iyzipay.HttpClient
  @Test
  @Ignore
  public void pay_should_return_payment_with_correct_values() throws MalformedURLException, Exception {
    // given
    Mockito.when(paymentService.applicationConfiguration.getPaymentApiKey()).thenReturn("API_KEY");
    Mockito.when(paymentService.applicationConfiguration.getPaymentApiSecretKey()).thenReturn("API_SECRET");
    Mockito.when(paymentService.applicationConfiguration.getPaymentApiUrl())
        .thenReturn("https://sandbox-api.iyzipay.com");

    Product product = new Product(1L, "Test", "Test", new BigDecimal("10"), 10L, null);
    Basket basket = new Basket(1L, null, null, BasketStatus.NOT_PAYED);
    basket.setProducts(new HashSet<Product>());

    basket.getProducts().add(product);
    basket.getProducts().add(product);
    basket.getProducts().add(product);

    // when
    Payment expectedPayment = this.paymentService.pay(basket);

    // then
    assertThat(expectedPayment.getPrice()).isEqualTo(new BigDecimal("30"));
    assertThat(expectedPayment.getPaidPrice()).isEqualTo(new BigDecimal("30"));
    assertThat(expectedPayment.getConversationId()).isEqualTo("123456789");
    assertThat(expectedPayment.getBasketId()).isEqualTo(1L);
  }

}