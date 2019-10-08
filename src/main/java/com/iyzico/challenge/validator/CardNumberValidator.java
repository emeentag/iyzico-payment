package com.iyzico.challenge.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.iyzico.challenge.validator.constraint.ValidCardNumber;

/**
 * CardNumberValidator
 */
public class CardNumberValidator implements ConstraintValidator<ValidCardNumber, String> {

  @Override
  public boolean isValid(String cardNumber, ConstraintValidatorContext context) {
    return isValidCardNumber(cardNumber);
  }

  /**
   * If card number holds only numbers then it is not formatted.
   * 
   * @param cardNumber
   * @return
   */
  public boolean isFormattedCardNumber(String cardNumber) {
    return !cardNumber.matches("^[0-9]*$");
  }

  public boolean isValidCardNumber(String cardNumber) {
    boolean isValid = false;
    int minLength = 15;
    int maxLength = 16;
    boolean isFormatted = isFormattedCardNumber(cardNumber);

    if (isFormatted) {
      cardNumber = cardNumber.replace("-", "");
    }

    if ((cardNumber.length() == minLength || cardNumber.length() == maxLength) && cardNumber.matches("^[0-9]*$")) {
      isValid = true;
    }
    return isValid;
  }

}