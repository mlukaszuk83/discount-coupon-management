package com.jml.coupon.domain.exception;

public class CouponNotFoundException extends DomainException {

  public CouponNotFoundException() {
    super(DomainExceptionCode.COUPON_NOT_FOUND, "Coupon with given code was not found");
  }
}