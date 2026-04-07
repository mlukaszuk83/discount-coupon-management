package com.jml.coupon.infrastructure.persistence.adapter;

import com.jml.coupon.domain.CouponRepository;
import com.jml.coupon.domain.model.Country;
import com.jml.coupon.domain.model.Coupon;
import com.jml.coupon.domain.model.CouponCode;
import com.jml.coupon.infrastructure.persistence.entity.CouponEntity;
import com.jml.coupon.infrastructure.persistence.springdata.JpaCouponRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
class CouponRepositoryAdapter implements CouponRepository {

  private final JpaCouponRepository jpaCouponRepository;

  @Override
  public Long save(Coupon coupon) {
    CouponEntity entity = toEntity(coupon);
    entity = jpaCouponRepository.save(entity);
    return entity.getId();
  }

  @Override
  public Optional<Coupon> findByCode(CouponCode couponCode) {
    return jpaCouponRepository.findByCode(couponCode.value())
        .map(this::toCoupon);
  }

  @Override
  public boolean incrementIfAvailable(CouponCode couponCode) {
    return jpaCouponRepository.increment(couponCode.value()) > 0;
  }

  @NonNull
  private CouponEntity toEntity(Coupon coupon) {
    final CouponEntity entity = new CouponEntity();
    entity.setCode(coupon.getCode().value());
    entity.setCountry(coupon.getCountry().code());
    entity.setMaxUses(coupon.getMaxUses());
    entity.setCurrentUses(coupon.getCurrentUses());
    entity.setCreatedAt(coupon.getCreatedAt());
    return entity;
  }

  @NonNull
  private Coupon toCoupon(CouponEntity entity) {
    final Coupon coupon = new Coupon(new CouponCode(entity.getCode()), new Country(entity.getCountry()), entity.getMaxUses());
    coupon.setId(entity.getId());
    coupon.setCurrentUses(entity.getCurrentUses());
    coupon.setCreatedAt(entity.getCreatedAt());
    return coupon;
  }
}