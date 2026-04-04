package com.jml.coupon.infrastructure.persistence.springdata;

import com.jml.coupon.infrastructure.persistence.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JpaCouponRepository extends JpaRepository<CouponEntity, Long> {

  Optional<CouponEntity> findByCode(String couponCode);

  @Modifying
  @Query("""
        UPDATE CouponEntity c
        SET c.currentUses = c.currentUses + 1
        WHERE c.code= :code
          AND c.currentUses < c.maxUses
      """)
  int increment(String code);
}