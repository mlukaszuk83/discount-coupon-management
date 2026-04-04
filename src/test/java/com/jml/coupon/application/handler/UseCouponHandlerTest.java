package com.jml.coupon.application.handler;

import com.jml.coupon.application.GeoService;
import com.jml.coupon.application.command.UseCouponCommand;
import com.jml.coupon.domain.CouponRepository;
import com.jml.coupon.domain.CouponUsageRepository;
import com.jml.coupon.domain.exception.CouponAlreadyUsedException;
import com.jml.coupon.domain.exception.CouponLimitReachedException;
import com.jml.coupon.domain.exception.CouponNotFoundException;
import com.jml.coupon.domain.exception.InvalidCountryException;
import com.jml.coupon.domain.model.Country;
import com.jml.coupon.domain.model.Coupon;
import com.jml.coupon.domain.model.CouponCode;
import com.jml.coupon.domain.model.UserId;
import org.assertj.core.api.Assertions;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UseCouponHandlerTest {

  private static final String IP = "1.2.3.4";
  private static final Country COUNTRY = new Country("country-code");
  private static final UserId USER_ID = new UserId("user-id");

  @Mock
  private CouponRepository couponRepository;

  @Mock
  private CouponUsageRepository couponUsageRepository;

  @Mock
  private GeoService geoService;

  @InjectMocks
  private UseCouponHandler systemUnderTest;

  @Test
  void givenValidCouponCodeNotUsedByUser_shouldIncrementCurrentUseCount_thenSaveUserUsage() {

    // given
    String code = "unused-coupon-code";
    CouponCode couponCode = new CouponCode(code);
    Coupon coupon = new Coupon(couponCode, COUNTRY, 5);

    when(couponRepository.findByCode(couponCode)).thenReturn(Optional.of(coupon));
    when(geoService.resolve(IP)).thenReturn(COUNTRY);
    when(couponUsageRepository.exists(coupon, USER_ID)).thenReturn(false);
    when(couponRepository.incrementIfAvailable(couponCode)).thenReturn(true);

    // when
    systemUnderTest.handle(createCommand(code), IP);

    // then
    InOrder inOrder = Mockito.inOrder(couponRepository, couponUsageRepository, geoService);
    inOrder.verify(couponRepository)
        .findByCode(couponCode);
    inOrder.verify(geoService)
        .resolve(IP);
    inOrder.verify(couponUsageRepository)
        .exists(coupon, USER_ID);
    inOrder.verify(couponRepository)
        .incrementIfAvailable(couponCode);
    inOrder.verify(couponUsageRepository)
        .save(coupon, USER_ID);
  }

  @Test
  void givenCouponDoesNotExistForGivenCode_thenCouponNotFoundExceptionIsThrown() {

    // given
    String code = "non-existing-coupon-code";
    when(couponRepository.findByCode(new CouponCode(code))).thenReturn(Optional.empty());

    // when / then
    Assertions.assertThatExceptionOfType(CouponNotFoundException.class)
        .isThrownBy(() -> systemUnderTest.handle(createCommand(code), IP))
        .withMessage("Coupon with given code was not found");
  }

  @Test
  void givenCouponCountryIsDifferentThanUserCountry_thenInvalidCountryExceptionIsThrown() {

    // given
    String code = "coupon-code";
    CouponCode couponCode = new CouponCode(code);
    Coupon coupon = new Coupon(couponCode, new Country("coupon-country-code"), 5);
    when(couponRepository.findByCode(couponCode)).thenReturn(Optional.of(coupon));

    String ip = "1.3.5.7";
    when(geoService.resolve(ip)).thenReturn(new Country("user-country-code"));

    // when / then
    Assertions.assertThatExceptionOfType(InvalidCountryException.class)
        .isThrownBy(() -> systemUnderTest.handle(createCommand(code), ip))
        .withMessage("Given coupon country is different than user country of origin");
  }

  @Test
  void givenUserHasAlreadyUsedTheCoupon_thenCouponAlreadyUsedExceptionIsThrown() {

    // given
    String code = "already-used-coupon-code";
    CouponCode couponCode = new CouponCode(code);
    Coupon coupon = new Coupon(couponCode, COUNTRY, 5);

    when(couponRepository.findByCode(couponCode)).thenReturn(Optional.of(coupon));
    when(geoService.resolve(IP)).thenReturn(COUNTRY);
    when(couponUsageRepository.exists(coupon, USER_ID)).thenReturn(true);

    // when / then
    Assertions.assertThatExceptionOfType(CouponAlreadyUsedException.class)
        .isThrownBy(() -> systemUnderTest.handle(createCommand(code), IP))
        .withMessage("Coupon with given code was already used by given user");
  }

  @Test
  void givenTheCouponHasReachedMaximumNumberOfUsages_thenCouponLimitReachedExceptionIsThrown() {

    // given
    String code = "max-number-of-usages-reached-coupon-code";
    CouponCode couponCode = new CouponCode(code);
    Coupon coupon = new Coupon(couponCode, COUNTRY, 5);

    when(couponRepository.findByCode(couponCode)).thenReturn(Optional.of(coupon));
    when(geoService.resolve(IP)).thenReturn(COUNTRY);
    when(couponUsageRepository.exists(coupon, USER_ID)).thenReturn(false);
    when(couponRepository.incrementIfAvailable(couponCode)).thenReturn(false);

    // when / then
    Assertions.assertThatExceptionOfType(CouponLimitReachedException.class)
        .isThrownBy(() -> systemUnderTest.handle(createCommand(code), IP))
        .withMessage("Coupon can't be used anymore, the usage limit has been reached");
  }

  @Test
  void givenTheUserTriedToUseTheSameCouponCoupleOfTimesInTheSameMoment_thenCouponAlreadyUsedExceptionIsThrown() {

    // given
    String code = "max-number-of-usages-reached-coupon-code";
    CouponCode couponCode = new CouponCode(code);
    Coupon coupon = new Coupon(couponCode, COUNTRY, 5);

    when(couponRepository.findByCode(couponCode)).thenReturn(Optional.of(coupon));
    when(geoService.resolve(IP)).thenReturn(COUNTRY);
    when(couponUsageRepository.exists(coupon, USER_ID)).thenReturn(false);
    when(couponRepository.incrementIfAvailable(couponCode)).thenReturn(true);

    doThrow(DataIntegrityViolationException.class).when(couponUsageRepository)
        .save(coupon, USER_ID);

    // when / then
    Assertions.assertThatExceptionOfType(CouponAlreadyUsedException.class)
        .isThrownBy(() -> systemUnderTest.handle(createCommand(code), IP))
        .withMessage("Coupon with given code was already used by given user");
  }

  @NonNull
  private UseCouponCommand createCommand(String code) {
    return new UseCouponCommand(code, USER_ID.id());
  }
}