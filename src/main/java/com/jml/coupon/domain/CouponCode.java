package com.jml.coupon.domain;

public record CouponCode(String value) {

  public CouponCode {
    value = value.toLowerCase();
  }
}