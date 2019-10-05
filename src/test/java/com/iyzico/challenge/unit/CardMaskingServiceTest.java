package com.iyzico.challenge.unit;

import static org.assertj.core.api.Assertions.assertThat;

import com.iyzico.challenge.service.CardMaskingService;
import com.iyzico.challenge.validators.CardNumberValidator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CardMaskingService.class)
public class CardMaskingServiceTest {

    private CardMaskingService cardMaskingService;

    @Mock
    private CardNumberValidator cardNumberValidator;

    @Before
    public void setUp() {
        cardMaskingService = new CardMaskingService();
    }

    @Test
    public void should_mask_digits_for_basic_credit_cards() throws Exception {
        // given
        String cardNumber = "4729150000000005";
        
        Mockito.when(cardNumberValidator.isValidCardNumber(cardNumber)).thenReturn(true);
        Mockito.when(cardNumberValidator.isFormattedCardNumber(cardNumber)).thenReturn(false);

        PowerMockito.whenNew(CardNumberValidator.class).withNoArguments().thenReturn(cardNumberValidator);

        // when
        String maskedCardNumber = cardMaskingService.maskCardNumber(cardNumber);

        // then
        assertThat(maskedCardNumber).isEqualTo("472915******0005");
    }

    @Test
    public void should_mask_digits_for_credit_cards_in_different_format() throws Exception {
        // given
        String cardNumber = "4729-1500-0000-0005";

        Mockito.when(cardNumberValidator.isValidCardNumber(cardNumber)).thenReturn(true);
        Mockito.when(cardNumberValidator.isFormattedCardNumber(cardNumber)).thenReturn(true);

        PowerMockito.whenNew(CardNumberValidator.class).withAnyArguments().thenReturn(cardNumberValidator);

        // when
        String maskedCardNumber = cardMaskingService.maskCardNumber(cardNumber);

        // then
        assertThat(maskedCardNumber).isEqualTo("4729-15**-****-0005");
    }

    @Test
    public void should_not_mask_anything_for_non_numeric_characters() throws Exception {
        // given
        String cardNumber = "John Doe";

        Mockito.when(cardNumberValidator.isValidCardNumber(cardNumber)).thenReturn(false);

        PowerMockito.whenNew(CardNumberValidator.class).withNoArguments().thenReturn(cardNumberValidator);

        // when
        String maskedCardNumber = cardMaskingService.maskCardNumber(cardNumber);

        // then
        assertThat(maskedCardNumber).isEqualTo("John Doe");
    }
}
