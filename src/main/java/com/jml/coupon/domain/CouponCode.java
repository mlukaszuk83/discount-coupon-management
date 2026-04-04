package com.jml.coupon.domain;

import org.apache.commons.lang3.StringUtils;

public record CouponCode(String value) {

  public CouponCode {

    value = StringUtils.trimToNull(value);
    if (value == null) {
      throw new EmptyCouponCodeException();
    }
    value = value.toLowerCase();
  }
}