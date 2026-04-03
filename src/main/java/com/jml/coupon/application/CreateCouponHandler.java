package com.jml.coupon.application;

import com.jml.coupon.domain.Country;
import com.jml.coupon.domain.Coupon;
import com.jml.coupon.domain.CouponCode;
import com.jml.coupon.domain.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CreateCouponHandler {

  private final CouponRepository couponRepository;

  @Transactional
  public void handle(CreateCouponCommand cmd) {
    final Coupon coupon = buildCoupon(cmd);
    couponRepository.save(coupon);
  }

  @NonNull
  private Coupon buildCoupon(CreateCouponCommand cmd) {
    return new Coupon(new CouponCode(cmd.couponCode()), new Country(cmd.countryCode()), cmd.maxUses());
  }
}