package com.jml.coupon.application;

import com.jml.coupon.domain.CouponRepository;
import com.jml.coupon.domain.CouponUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class UseCouponHandler {

  private final CouponRepository couponRepository;

  public String handle(UseCouponCommand cmd, String ip) {
    return null;
  }
}