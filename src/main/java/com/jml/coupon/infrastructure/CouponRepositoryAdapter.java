package com.jml.coupon.infrastructure;

import com.jml.coupon.domain.Coupon;
import com.jml.coupon.domain.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
class CouponRepositoryAdapter implements CouponRepository {

  private final JpaCouponRepository jpaCouponRepository;

  @Override
  public void save(Coupon coupon) {
    final CouponEntity entity = buildEntity(coupon);
    jpaCouponRepository.save(entity);
  }

  @NonNull
  private CouponEntity buildEntity(Coupon coupon) {
    final CouponEntity entity = new CouponEntity();
    entity.setCode(coupon.getCode().value());
    entity.setCountry(coupon.getCountry().code());
    entity.setMaxUses(coupon.getMaxUses());
    entity.setCurrentUses(coupon.getCurrentUses());
    entity.setCreatedAt(coupon.getCreatedAt());
    return entity;
  }
}