package com.iyzico.challenge.middleware.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * EntityNotAcceptable
 */
@ResponseStatus(value = HttpStatus.FAILED_DEPENDENCY, code = HttpStatus.FAILED_DEPENDENCY)
public class PaymentFailedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public PaymentFailedException() {
    super();
  }

  public PaymentFailedException(String message) {
    super(message);
  }

  public PaymentFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}