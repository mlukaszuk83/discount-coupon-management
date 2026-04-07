package com.jml.coupon.infrastructure.persistence.adapter;

import com.jml.coupon.domain.model.Country;
import com.jml.coupon.domain.model.Coupon;
import com.jml.coupon.domain.model.CouponCode;
import com.jml.coupon.infrastructure.persistence.entity.CouponEntity;
import com.jml.coupon.infrastructure.persistence.springdata.JpaCouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponRepositoryAdapterTest {

  @Mock
  private JpaCouponRepository jpaCouponRepository;

  @InjectMocks
  private CouponRepositoryAdapter systemUnderTest;

  @Test
  void save_givenCouponToSave_shouldConvertItToEntity_thenSaveInRepositoryAndReturnId() {

    // given
    long couponId = 1;
    String couponCode = "coupon-code";
    String couponCountry = "country-code";
    Coupon coupon = new Coupon(new CouponCode(couponCode), new Country(couponCountry), 10);
    CouponEntity entity = mock(CouponEntity.class);
    when(entity.getId()).thenReturn(couponId);
    when(jpaCouponRepository.save(any(CouponEntity.class))).thenReturn(entity);

    // when
    Long result = systemUnderTest.save(coupon);

    // then
    assertThat(result).isEqualTo(couponId);
    ArgumentCaptor<CouponEntity> couponEntityArgumentCaptor = ArgumentCaptor.forClass(CouponEntity.class);
    verify(jpaCouponRepository).save(couponEntityArgumentCaptor.capture());

    CouponEntity savedCoupon = couponEntityArgumentCaptor.getValue();
    assertThat(savedCoupon).extracting(CouponEntity::getCode, CouponEntity::getCountry, CouponEntity::getMaxUses, CouponEntity::getCurrentUses, CouponEntity::getCreatedAt)
        .containsExactly(couponCode, couponCountry.toUpperCase(), coupon.getMaxUses(), coupon.getCurrentUses(), coupon.getCreatedAt());
  }

  @Test
  void findByCode_givenCouponCode_whenJpaRepositoryReturnsEmptyResult_thenEmptyOptionalIsReturned() {

    // given
    String code = "non-existing-coupon-code";
    when(jpaCouponRepository.findByCode(anyString())).thenReturn(Optional.empty());

    // when
    Optional<Coupon> result = systemUnderTest.findByCode(new CouponCode(code));

    // then
    assertThat(result).isEmpty();
    verify(jpaCouponRepository).findByCode(code);
  }

  @Test
  void findByCode_givenCouponCode_whenJpaRepositoryReturnsCouponEntity_thenOptionalWithCouponIsReturned() {

    // given
    String code = "existing-coupon-code";
    Instant now = Instant.now();

    CouponEntity entity = mock(CouponEntity.class);
    when(entity.getId()).thenReturn(1L);
    when(entity.getCode()).thenReturn(code);
    when(entity.getCountry()).thenReturn("country-code");
    when(entity.getMaxUses()).thenReturn(10);
    when(entity.getCurrentUses()).thenReturn(5);
    when(entity.getCreatedAt()).thenReturn(now);

    when(jpaCouponRepository.findByCode(anyString())).thenReturn(Optional.of(entity));

    CouponCode couponCode = new CouponCode(code);
    Coupon expectedResult = new Coupon(couponCode, new Country("country-code"), 10);
    expectedResult.setId(1L);
    expectedResult.setCurrentUses(5);
    expectedResult.setCreatedAt(now);

    // when
    Optional<Coupon> result = systemUnderTest.findByCode(couponCode);

    // then
    assertThat(result).contains(expectedResult);
    verify(jpaCouponRepository).findByCode(code);
  }

  @Test
  void incrementIfAvailable_whenJpaRepositoryReturnsZero_thenFalseIsReturned() {

    // given
    CouponCode couponCode = new CouponCode("incrementation-not-possible-code");
    when(jpaCouponRepository.increment(anyString())).thenReturn(0);

    // when
    boolean result = systemUnderTest.incrementIfAvailable(couponCode);

    // then
    assertThat(result).isFalse();
    verify(jpaCouponRepository).increment(couponCode.value());
  }

  @Test
  void incrementIfAvailable_whenJpaRepositoryReturnsMoreThanZero_thenTrueIsReturned() {

    // given
    CouponCode couponCode = new CouponCode("incrementation-possible-code");
    when(jpaCouponRepository.increment(anyString())).thenReturn(1);

    // when
    boolean result = systemUnderTest.incrementIfAvailable(couponCode);

    // then
    assertThat(result).isTrue();
    verify(jpaCouponRepository).increment(couponCode.value());
  }
}