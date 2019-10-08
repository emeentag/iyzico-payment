package com.iyzico.challenge.middleware.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * EntityNotAcceptable
 */
@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED, code = HttpStatus.EXPECTATION_FAILED)
public class EntityNotAcceptableException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public EntityNotAcceptableException() {
    super();
  }

  public EntityNotAcceptableException(String message) {
    super(message);
  }

  public EntityNotAcceptableException(String message, Throwable cause) {
    super(message, cause);
  }
}