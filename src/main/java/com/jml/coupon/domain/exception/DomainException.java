package com.jml.coupon.domain.exception;

import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException {

  private final DomainExceptionCode code;

  public DomainException(DomainExceptionCode code, String message) {
    super(message);
    this.code = code;
  }
}