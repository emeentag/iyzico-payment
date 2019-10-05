package com.iyzico.challenge.units;

import static org.assertj.core.api.Assertions.assertThat;

import com.iyzico.challenge.validators.CardNumberValidator;

import org.junit.Before;
import org.junit.Test;

/**
 * CardNumberValidatorTest
 */
public class CardNumberValidatorTest {

  private CardNumberValidator cardNumberValidator;

  @Before
  public void setUp() {
    cardNumberValidator = new CardNumberValidator();
  }

  @Test
  public void isValid_should_return_true_when_valid_cardnumber_given() {

    // given
    String cardNumber = "4729150000000005";

    // when
    boolean isValid = cardNumberValidator.isValid(cardNumber, null);

    // then
    assertThat(isValid).isTrue();

  }

  @Test
  public void isValid_should_return_true_when_valid_formatted_cardnumber_given() {

    // given
    String cardNumber = "4729-1500-0000-0005";

    // when
    boolean isValid = cardNumberValidator.isValid(cardNumber, null);

    // then
    assertThat(isValid).isTrue();

  }

  @Test
  public void isValid_should_return_false_when_invalid_cardnumber_given() {

    // given
    String lessThan15NumCardNumber = "4729150000005";
    String lessThan15NumFormattedCardNumber = "4729-150-0000-005";
    String malformedCardNumber = "47291500A0000005";
    String malformedFormattedCardNumber = "4729-1D00-0000-0005";
    String malformedFormattedCardNumber2 = "4729/1500/0000/0005";

    // when
    boolean isValid1 = cardNumberValidator.isValid(lessThan15NumCardNumber, null);
    boolean isValid2 = cardNumberValidator.isValid(lessThan15NumFormattedCardNumber, null);
    boolean isValid3 = cardNumberValidator.isValid(malformedCardNumber, null);
    boolean isValid4 = cardNumberValidator.isValid(malformedFormattedCardNumber, null);
    boolean isValid5 = cardNumberValidator.isValid(malformedFormattedCardNumber2, null);

    // then
    assertThat(isValid1).isFalse();
    assertThat(isValid2).isFalse();
    assertThat(isValid3).isFalse();
    assertThat(isValid4).isFalse();
    assertThat(isValid5).isFalse();
  }

  @Test
  public void isValid_should_return_the_same_value_with_isValidCardNumber() {

    // given
    String cardNumber = "4729150000000005";

    // when
    boolean isValid = cardNumberValidator.isValid(cardNumber, null);
    boolean isValidCardNumber = cardNumberValidator.isValidCardNumber(cardNumber);

    // then
    assertThat(isValid).isEqualTo(isValidCardNumber);

  }

  @Test
  public void isFormattedCardNumber_should_return_true_when_formatted_cardnumber_given() {

    // given
    String cardNumber = "4729-1500-0000-0005";

    // when
    boolean isValid = cardNumberValidator.isFormattedCardNumber(cardNumber);

    // then
    assertThat(isValid).isTrue();
  }

  @Test
  public void isFormattedCardNumber_should_return_false_when_unformatted_cardnumber_given() {

    // given
    String cardNumber = "4729150000000005";

    // when
    boolean isValid = cardNumberValidator.isFormattedCardNumber(cardNumber);

    // then
    assertThat(isValid).isFalse();
  }
}