package com.jml.coupon.domain.exception;

public class CouponAlreadyUsedException extends DomainException {

  public CouponAlreadyUsedException() {
    super(DomainExceptionCode.COUPON_USED, "Coupon with given code was already used by given user");
  }
}