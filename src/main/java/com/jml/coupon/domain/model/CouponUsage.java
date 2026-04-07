package com.jml.coupon.domain.model;

import com.jml.coupon.domain.exception.EmptyUserIdException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

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

  public void validateUser() {
    if (StringUtils.isBlank(userId)) {
      throw new EmptyUserIdException();
    }
  }
}