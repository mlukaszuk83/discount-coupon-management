package com.jml.coupon.application.handler;

import com.jml.coupon.application.command.CreateCouponCommand;
import com.jml.coupon.domain.CouponRepository;
import com.jml.coupon.domain.exception.CouponAlreadyExistsException;
import com.jml.coupon.domain.model.Coupon;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateCouponHandlerTest {

  @Mock
  private CouponRepository couponRepository;

  @InjectMocks
  private CreateCouponHandler systemUnderTest;

  @Test
  void givenCreateCouponCommand_shouldBuildNewCoupon_thenSaveInRepository() {

    // given
    Instant before = Instant.now();
    CreateCouponCommand cmd = new CreateCouponCommand("COUPON-code", "country-CODE", 10);

    // when
    systemUnderTest.handle(cmd);

    // then
    Instant after = Instant.now();
    ArgumentCaptor<Coupon> couponArgumentCaptor = ArgumentCaptor.forClass(Coupon.class);
    verify(couponRepository).save(couponArgumentCaptor.capture());

    Coupon savedCoupon = couponArgumentCaptor.getValue();
    assertThat(savedCoupon.getCode()
        .value()).isEqualTo("coupon-code");
    assertThat(savedCoupon.getCountry()
        .code()).isEqualTo("COUNTRY-CODE");
    assertThat(savedCoupon.getMaxUses()).isEqualTo(10);
    assertThat(savedCoupon.getCurrentUses()).isZero();
    assertThat(savedCoupon.getCreatedAt()).isBetween(before, after);
  }

  @Test
  void givenCreateCouponCommand_whenCouponWithGivenCodeAlreadyExists_thenCouponAlreadyExistsExceptionIsThrown() {

    // given
    CreateCouponCommand cmd = new CreateCouponCommand("duplicated-code", "country-code", 1);
    doThrow(DataIntegrityViolationException.class).when(couponRepository)
        .save(any(Coupon.class));

    // when
    assertThatExceptionOfType(CouponAlreadyExistsException.class).isThrownBy(() -> systemUnderTest.handle(cmd))
        .withMessage("A coupon with given code already exists");
  }
}