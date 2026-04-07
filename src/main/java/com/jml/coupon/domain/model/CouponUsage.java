package com.jml.coupon.domain.model;

import lombok.Data;

import java.time.Instant;

@Data
public class CouponUsage {

  private Long id;
  private Coupon coupon;
  private String userId;
  private Instant usedAt;

  public CouponUsage(Coupon coupon, String userId) {
    this.coupon = coupon;
    this.userId = userId;
    this.usedAt = Instant.now();
  }

  public Long getCouponId() {
    return coupon.getId();
  }
}