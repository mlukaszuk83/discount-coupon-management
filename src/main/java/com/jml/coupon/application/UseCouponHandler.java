package com.jml.coupon.application;

import com.jml.coupon.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
public class UseCouponHandler {

  private final CouponRepository couponRepository;
  private final CouponUsageRepository couponUsageRepository;
  private final GeoService geoService;

  @Transactional
  public void handle(UseCouponCommand cmd, String ip) {

    log.info("Coupon usage handling started");
    final CouponCode couponCode = new CouponCode(cmd.couponCode());
    final Coupon coupon = couponRepository.findByCode(couponCode)
        .orElseThrow(CouponNotFoundException::new);

    log.debug("Found coupon: {} for code: {}", coupon, couponCode);
    final Country country = geoService.resolve(ip);
    coupon.validateCountry(country);

    final UserId userId = new UserId(cmd.userId());
    if (couponUsageRepository.exists(coupon, userId)) {
      throw new CouponAlreadyUsedException();
    }

    log.debug("Coupon not used by user: [{}] yet, checking if still available", userId);
    final boolean updated = couponRepository.incrementIfAvailable(couponCode);
    if (!updated) {
      throw new CouponLimitReachedException();
    }

    try {
      log.debug("Coupon not used by user yet and available, saving new usage entry");
      couponUsageRepository.save(coupon, userId);
    } catch (DataIntegrityViolationException ex) {
      log.debug("User: {} tried to use the coupon: {} more than once in a same moment", userId, coupon);
      // UNIQUE constraint hit -> user already used this coupon
      throw new CouponAlreadyUsedException();
    } finally {
      log.info("Coupon usage handling finished");
    }
  }
}