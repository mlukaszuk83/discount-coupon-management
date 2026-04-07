package com.jml.coupon.infrastructure.persistence.adapter;

import com.jml.coupon.domain.model.Coupon;
import com.jml.coupon.domain.model.CouponUsage;
import com.jml.coupon.infrastructure.persistence.entity.CouponUsageEntity;
import com.jml.coupon.infrastructure.persistence.springdata.JpaCouponUsageRepository;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponUsageRepositoryAdapterTest {

  private static final Coupon COUPON = createCoupon();
  private static final String USER_ID = "user-id";

  @Mock
  private JpaCouponUsageRepository jpaCouponUsageRepository;

  @InjectMocks
  private CouponUsageRepositoryAdapter systemUnderTest;

  @Test
  void exists_returnsTheResultOfJpaRepositoryQuery() {

    // given
    when(jpaCouponUsageRepository.existsByCouponIdAndUserId(anyLong(), anyString())).thenReturn(true);

    // when
    boolean result = systemUnderTest.exists(new CouponUsage(COUPON, USER_ID));

    // then
    assertThat(result).isTrue();
    verify(jpaCouponUsageRepository).existsByCouponIdAndUserId(COUPON.getId(), USER_ID);
  }

  @Test
  void save_givenCouponAndUserIdToSave_shouldConvertItToEntity_thenSaveInRepository() {

    // given
    long couponUsageId = 1;
    CouponUsageEntity entity = mock(CouponUsageEntity.class);
    when(entity.getId()).thenReturn(couponUsageId);
    when(jpaCouponUsageRepository.save(any(CouponUsageEntity.class))).thenReturn(entity);

    // when
    Instant before = Instant.now();
    Long result = systemUnderTest.save(new CouponUsage(COUPON, USER_ID));
    Instant after = Instant.now();

    // then
    assertThat(result).isEqualTo(couponUsageId);

    ArgumentCaptor<CouponUsageEntity> couponEntityArgumentCaptor = ArgumentCaptor.forClass(CouponUsageEntity.class);
    verify(jpaCouponUsageRepository).save(couponEntityArgumentCaptor.capture());

    CouponUsageEntity savedCouponUsage = couponEntityArgumentCaptor.getValue();
    assertThat(savedCouponUsage.getCouponId()).isEqualTo(COUPON.getId());
    assertThat(savedCouponUsage.getUserId()).isEqualTo(USER_ID);
    assertThat(savedCouponUsage.getUsedAt()).isBetween(before, after);
  }

  @NonNull
  private static Coupon createCoupon() {
    Coupon coupon = new Coupon(null, null, 0);
    coupon.setId(1L);
    return coupon;
  }
}