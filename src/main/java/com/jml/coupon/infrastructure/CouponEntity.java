package com.jml.coupon.infrastructure;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "coupons", uniqueConstraints = {
    @UniqueConstraint(columnNames = "code")
})
class CouponEntity {

  @Id
  @GeneratedValue
  private Long id;
  private String code;
  private Instant createdAt;
  private int maxUses;
  private int currentUses;
  private String country;

}