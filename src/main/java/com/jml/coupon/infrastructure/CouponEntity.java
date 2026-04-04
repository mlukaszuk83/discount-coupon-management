package com.jml.coupon.infrastructure;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "coupons", uniqueConstraints = {
    @UniqueConstraint(columnNames = "code")
})
@Data
class CouponEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;
  private String code;
  private Instant createdAt;
  private int maxUses;
  private int currentUses;
  private String country;

}