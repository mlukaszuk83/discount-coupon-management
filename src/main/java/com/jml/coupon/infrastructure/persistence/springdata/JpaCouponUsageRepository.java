package com.jml.coupon.infrastructure.persistence.springdata;

import com.jml.coupon.infrastructure.persistence.entity.CouponUsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCouponUsageRepository extends JpaRepository<CouponUsageEntity, Long> {

  boolean existsByCouponIdAndUserId(Long couponId, String userId);
}