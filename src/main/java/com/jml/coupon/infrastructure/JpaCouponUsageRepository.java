package com.jml.coupon.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCouponUsageRepository extends JpaRepository<CouponUsageEntity, Long> {

  boolean existsByCouponIdAndUserId(Long couponId, String userId);
}