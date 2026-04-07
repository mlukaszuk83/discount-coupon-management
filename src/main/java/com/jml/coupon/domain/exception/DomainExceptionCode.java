package com.jml.coupon.domain.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum DomainExceptionCode {

  COUPON_EXISTS("coupon-already-exists"),
  COUPON_USED("coupon-already-used"),
  COUPON_LIMIT_REACHED("coupon-limit-reached"),
  COUPON_NOT_FOUND("coupon-not-found"),
  COUPON_CODE_EMPTY("empty-coupon-code"),
  COUNTRY_EMPTY("empty-country-code"),
  COUNTRY_INVALID("invalid-country"),
  USER_EMPTY("empty-user-id");

  private final String type;
}