package com.jml.coupon.infrastructure.persistence.adapter;

import com.jml.coupon.domain.CouponUsageRepository;
import com.jml.coupon.domain.model.CouponUsage;
import com.jml.coupon.infrastructure.persistence.entity.CouponUsageEntity;
import com.jml.coupon.infrastructure.persistence.springdata.JpaCouponUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
class CouponUsageRepositoryAdapter implements CouponUsageRepository {

  private final JpaCouponUsageRepository jpaCouponUsageRepository;

  @Override
  public boolean exists(CouponUsage couponUsage) {
    return jpaCouponUsageRepository.existsByCouponIdAndUserId(couponUsage.getCouponId(), couponUsage.getUserId());
  }

  @Override
  public Long save(CouponUsage couponUsage) {
    CouponUsageEntity entity = toEntity(couponUsage);
    entity = jpaCouponUsageRepository.save(entity);
    return entity.getId();
  }

  private CouponUsageEntity toEntity(CouponUsage couponUsage) {
    final CouponUsageEntity entity = new CouponUsageEntity();
    entity.setCouponId(couponUsage.getCouponId());
    entity.setUserId(couponUsage.getUserId());
    entity.setUsedAt(couponUsage.getUsedAt());
    return entity;
  }
}