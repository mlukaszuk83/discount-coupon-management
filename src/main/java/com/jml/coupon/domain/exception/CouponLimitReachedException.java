package com.jml.coupon.domain.exception;

public class CouponLimitReachedException extends DomainException {

  public CouponLimitReachedException() {
    super(DomainExceptionCode.COUPON_LIMIT_REACHED, "This coupon can't be used anymore, the usage limit has been reached");
  }
}