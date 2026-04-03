package com.jml.coupon.application;

import com.jml.coupon.domain.Coupon;
import com.jml.coupon.domain.CouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
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
    Instant after = Instant.now();

    // when
    systemUnderTest.handle(cmd);

    // then
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
}