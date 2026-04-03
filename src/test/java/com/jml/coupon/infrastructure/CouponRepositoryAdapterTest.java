package com.jml.coupon.infrastructure;

import com.jml.coupon.domain.Country;
import com.jml.coupon.domain.Coupon;
import com.jml.coupon.domain.CouponCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CouponRepositoryAdapterTest {

  @Mock
  private JpaCouponRepository jpaCouponRepository;

  @InjectMocks
  private CouponRepositoryAdapter systemUnderTest;

  @Test
  void givenCouponToSave_shouldConvertItToEntity_thenSaveInRepository() {

    // given
    Coupon coupon = new Coupon(new CouponCode("coupon-code"), new Country("country-code"), 10);

    // when
    systemUnderTest.save(coupon);

    // then
    ArgumentCaptor<CouponEntity> couponEntityArgumentCaptor = ArgumentCaptor.forClass(CouponEntity.class);
    verify(jpaCouponRepository).save(couponEntityArgumentCaptor.capture());

    CouponEntity savedCoupon = couponEntityArgumentCaptor.getValue();
    assertThat(savedCoupon).extracting(CouponEntity::getCode, CouponEntity::getCountry, CouponEntity::getMaxUses, CouponEntity::getCurrentUses, CouponEntity::getCreatedAt)
        .containsExactly(coupon.getCode()
            .value(), coupon.getCountry()
            .code(), coupon.getMaxUses(), coupon.getCurrentUses(), coupon.getCreatedAt());
  }
}