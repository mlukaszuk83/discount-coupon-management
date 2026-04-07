package com.jml.coupon.application.handler;

import com.jml.coupon.application.GeoService;
import com.jml.coupon.application.dto.CouponUsageDto;
import com.jml.coupon.application.request.UseCouponRequest;
import com.jml.coupon.domain.CouponRepository;
import com.jml.coupon.domain.CouponUsageRepository;
import com.jml.coupon.domain.exception.CouponAlreadyUsedException;
import com.jml.coupon.domain.exception.CouponLimitReachedException;
import com.jml.coupon.domain.exception.CouponNotFoundException;
import com.jml.coupon.domain.model.Country;
import com.jml.coupon.domain.model.Coupon;
import com.jml.coupon.domain.model.CouponCode;
import com.jml.coupon.domain.model.CouponUsage;
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
  public CouponUsageDto handle(UseCouponRequest request, String ip) {

    log.info("Coupon usage handling started");
    log.debug("Request object: {}", request);
    final CouponCode couponCode = new CouponCode(request.code());
    final Coupon coupon = couponRepository.findByCode(couponCode)
        .orElseThrow(CouponNotFoundException::new);

    log.debug("Found coupon: {} for code: {}", coupon, couponCode);
    final Country country = geoService.resolve(ip);
    coupon.validateCountry(country);

    final String userId = request.userId();
    final CouponUsage couponUsage = new CouponUsage(coupon, userId);
    if (couponUsageRepository.exists(couponUsage)) {
      throw new CouponAlreadyUsedException();
    }

    log.debug("Coupon not used by user: [{}] yet, checking if still available", userId);
    final boolean updated = couponRepository.incrementIfAvailable(couponCode);
    if (!updated) {
      throw new CouponLimitReachedException();
    }

    try {
      log.debug("Coupon not used by user yet and available, saving new usage entry");
      final Long id = couponUsageRepository.save(couponUsage);
      return toDto(couponUsage, id);
    } catch (DataIntegrityViolationException ex) {
      log.debug("User: [{}] tried to use the coupon: {} more than once in a same moment", userId, coupon);
      // UNIQUE constraint hit -> user already used this coupon
      throw new CouponAlreadyUsedException();
    } finally {
      log.info("Coupon usage handling finished");
    }
  }

  private CouponUsageDto toDto(CouponUsage couponUsage, Long couponUsageId) {
    final Coupon coupon = couponUsage.getCoupon();
    final CouponCode couponCode = coupon.getCode();
    return new CouponUsageDto(couponUsageId, couponCode.value(), couponUsage.getUserId(), couponUsage.getUsedAt());
  }
}