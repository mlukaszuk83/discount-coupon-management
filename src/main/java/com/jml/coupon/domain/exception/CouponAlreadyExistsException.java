package com.jml.coupon.domain.exception;

public class CouponAlreadyExistsException extends DomainException {

  public CouponAlreadyExistsException() {
    super(DomainExceptionCode.COUPON_EXISTS, "A coupon with given code already exists");
  }
}