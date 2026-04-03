package com.jml.coupon.infrastructure;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "coupon_usage", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"coupon_id", "user_id"})
})
class CouponUsageEntity {

  @Id
  @GeneratedValue
  private Long id;
  private Long couponId;
  private String userId;
  private Instant usedAt;
}