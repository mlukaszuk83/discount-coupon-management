package com.jml.coupon.application;

import com.jml.coupon.domain.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CreateCouponHandler {

  private final CouponRepository couponRepository;

  public void handle(CreateCouponCommand cmd) {

  }
}