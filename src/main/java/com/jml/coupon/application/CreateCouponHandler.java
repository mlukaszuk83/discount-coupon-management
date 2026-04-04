package com.jml.coupon.application;

import com.jml.coupon.domain.*;
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
  public void handle(CreateCouponCommand cmd) {

    log.info("Coupon create handling started");
    try {
      final Coupon coupon = buildCoupon(cmd);
      couponRepository.save(coupon);
    } catch (DataIntegrityViolationException ex) {
      log.debug("Tried to create a coupon for code: [{}], but it already exists", cmd.couponCode());
      // UNIQUE constraint hit -> coupon with given code already exists
      throw new CouponAlreadyExistsException();
    } finally {
      log.info("Coupon create handling finished");
    }
  }

  @NonNull
  private Coupon buildCoupon(CreateCouponCommand cmd) {
    return new Coupon(new CouponCode(cmd.couponCode()), new Country(cmd.countryCode()), cmd.maxUses());
  }
}