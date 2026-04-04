package com.jml.coupon.infrastructure;

import com.jml.coupon.domain.Coupon;
import com.jml.coupon.domain.CouponUsageRepository;
import com.jml.coupon.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@RequiredArgsConstructor
@Repository
class CouponUsageRepositoryAdapter implements CouponUsageRepository {

  private final JpaCouponUsageRepository jpaCouponUsageRepository;

  @Override
  public boolean exists(Coupon coupon, UserId userId) {
    return jpaCouponUsageRepository.existsByCouponIdAndUserId(coupon.getId(), userId.id());
  }

  @Override
  public void save(Coupon coupon, UserId userId) {
    final CouponUsageEntity entity = toEntity(coupon, userId);
    jpaCouponUsageRepository.save(entity);
  }

  private CouponUsageEntity toEntity(Coupon coupon, UserId userId) {
    CouponUsageEntity entity = new CouponUsageEntity();
    entity.setCouponId(coupon.getId());
    entity.setUserId(userId.id());
    entity.setUsedAt(Instant.now());
    return entity;
  }
}