package com.iyzico.challenge.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CardMaskingServiceTest {

    @Autowired
    private CardMaskingService cardMaskingService;

    @Test
    public void should_mask_digits_for_basic_credit_cards() {
        //given
        String cardNumber = "4729150000000005";

        //when
        String maskedCardNumber = cardMaskingService.maskCardNumber(cardNumber);

        //then
        assertThat(maskedCardNumber).isEqualTo("472915******0005");
    }

    @Test
    public void should_mask_digits_for_credit_cards_in_different_format() {
        //given
        String cardNumber = "4729-1500-0000-0005";

        //when
        String maskedCardNumber = cardMaskingService.maskCardNumber(cardNumber);

        //then
        assertThat(maskedCardNumber).isEqualTo("4729-15**-****-0005");
    }

    @Test
    public void should_not_mask_anything_for_non_numeric_characters() {
        //given
        String cardNumber = "John Doe";

        //when
        String maskedCardNumber = cardMaskingService.maskCardNumber(cardNumber);

        //then
        assertThat(maskedCardNumber).isEqualTo("John Doe");
    }

    @Test
    public void should_return_provided_card_number_if_not_valid() {
        //given
        String cardNumber = "4729150000005";
        String formattedCardNumber = "4729-150-0000-005";

        //when
        String maskedCardNumber = cardMaskingService.maskCardNumber(cardNumber);
        String maskedFormattedCardNumber = cardMaskingService.maskCardNumber(formattedCardNumber);

        //then
        assertThat(maskedCardNumber).isEqualTo("4729150000005");
        assertThat(maskedFormattedCardNumber).isEqualTo("4729-150-0000-005");
    }
}
