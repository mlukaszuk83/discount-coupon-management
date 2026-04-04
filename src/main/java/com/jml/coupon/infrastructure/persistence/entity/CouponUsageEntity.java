package com.jml.coupon.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "coupon_usage", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"coupon_id", "user_id"})
})
@Data
public class CouponUsageEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;
  private Long couponId;
  private String userId;
  private Instant usedAt;
}