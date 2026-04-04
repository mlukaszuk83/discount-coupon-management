package com.jml.coupon.domain.exception;

public class EmptyCouponCodeException extends DomainException {

  public EmptyCouponCodeException() {
    super(DomainExceptionCode.COUPON_CODE_EMPTY, "Coupon code cannot be empty");
  }
}