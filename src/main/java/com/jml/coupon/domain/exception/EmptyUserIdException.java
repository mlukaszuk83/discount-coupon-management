package com.jml.coupon.domain.exception;

public class EmptyUserIdException extends DomainException {

  public EmptyUserIdException() {
    super(DomainExceptionCode.USER_EMPTY, "User id cannot be empty");
  }
}