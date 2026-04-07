package com.jml.coupon.application.handler;

import com.jml.coupon.application.dto.CouponDto;
import com.jml.coupon.application.request.CreateCouponRequest;
import com.jml.coupon.domain.CouponRepository;
import com.jml.coupon.domain.exception.CouponAlreadyExistsException;
import com.jml.coupon.domain.model.Country;
import com.jml.coupon.domain.model.Coupon;
import com.jml.coupon.domain.model.CouponCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
public class CreateCouponHandler {

  private final CouponRepository couponRepository;

  @Transactional
  public CouponDto handle(CreateCouponRequest request) {

    log.info("Coupon create handling started");
    log.debug("Request object: {}", request);
    try {
      final Coupon coupon = toCoupon(request);
      final Long id = couponRepository.save(coupon);
      return toDto(coupon, id);
    } catch (DataIntegrityViolationException ex) {
      log.debug("Tried to create a coupon for code: [{}], but it already exists", request.couponCode());
      // UNIQUE constraint hit -> coupon with given code already exists
      throw new CouponAlreadyExistsException();
    } finally {
      log.info("Coupon create handling finished");
    }
  }

  @NonNull
  private Coupon toCoupon(CreateCouponRequest request) {
    return new Coupon(new CouponCode(request.couponCode()), new Country(request.countryCode()), request.maxUses());
  }

  private CouponDto toDto(Coupon coupon, Long couponId) {
    final CouponCode couponCode = coupon.getCode();
    final Country couponCountry = coupon.getCountry();
    return new CouponDto(couponId, couponCode.value(), couponCountry.code(), coupon.getMaxUses(), coupon.getCurrentUses(), coupon.getCreatedAt());
  }
}