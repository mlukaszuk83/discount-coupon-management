package com.jml.coupon.application.handler;

import com.jml.coupon.application.dto.CouponDto;
import com.jml.coupon.application.request.CreateCouponRequest;
import com.jml.coupon.domain.CouponRepository;
import com.jml.coupon.domain.exception.CouponAlreadyExistsException;
import com.jml.coupon.domain.model.Country;
import com.jml.coupon.domain.model.Coupon;
import com.jml.coupon.domain.model.CouponCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCouponHandlerTest {

  @Mock
  private CouponRepository couponRepository;

  @InjectMocks
  private CreateCouponHandler systemUnderTest;

  @Test
  void givenCreateCouponCommand_shouldCreateNewCoupon_thenSaveInRepositoryAndReturnCouponDto() {

    // given
    long couponId = 1;
    Instant before = Instant.now();
    CreateCouponRequest request = new CreateCouponRequest("COUPON-code", "country-CODE", 10);
    when(couponRepository.save(any(Coupon.class))).thenReturn(couponId);

    // when
    CouponDto result = systemUnderTest.handle(request);

    // then
    Instant after = Instant.now();
    ArgumentCaptor<Coupon> couponArgumentCaptor = ArgumentCaptor.forClass(Coupon.class);
    verify(couponRepository).save(couponArgumentCaptor.capture());

    Coupon savedCoupon = couponArgumentCaptor.getValue();
    CouponCode couponCode = savedCoupon.getCode();
    Country couponCountry = savedCoupon.getCountry();

    assertThat(couponCode.value()).isEqualTo("coupon-code");
    assertThat(couponCountry.code()).isEqualTo("COUNTRY-CODE");
    assertThat(savedCoupon.getMaxUses()).isEqualTo(10);
    assertThat(savedCoupon.getCurrentUses()).isZero();
    assertThat(savedCoupon.getCreatedAt()).isBetween(before, after);

    assertThat(result).extracting(CouponDto::id,
            CouponDto::code,
            CouponDto::country,
            CouponDto::maxUses,
            CouponDto::currentUses,
            CouponDto::createdAt)
        .containsExactly(couponId,
            couponCode.value(),
            couponCountry.code(),
            savedCoupon.getMaxUses(),
            savedCoupon.getCurrentUses(),
            savedCoupon.getCreatedAt());
  }

  @Test
  void givenCreateCouponCommand_whenCouponWithGivenCodeAlreadyExists_thenCouponAlreadyExistsExceptionIsThrown() {

    // given
    CreateCouponRequest request = new CreateCouponRequest("duplicated-code", "country-code", 1);
    doThrow(DataIntegrityViolationException.class).when(couponRepository)
        .save(any(Coupon.class));

    // when
    assertThatExceptionOfType(CouponAlreadyExistsException.class).isThrownBy(() -> systemUnderTest.handle(request))
        .withMessage("A coupon with given code already exists");
  }
}