package com.jml.coupon.infrastructure;

import com.jml.coupon.domain.Coupon;
import com.jml.coupon.domain.UserId;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponUsageRepositoryAdapterTest {

  public static final Coupon COUPON = createCoupon();
  public static final UserId USER_ID = new UserId("user-id");
  @Mock
  private JpaCouponUsageRepository jpaCouponUsageRepository;

  @InjectMocks
  private CouponUsageRepositoryAdapter systemUnderTest;

  @Test
  void exists_returnsTheResultOfJpaRepositoryQuery() {

    // given
    when(jpaCouponUsageRepository.existsByCouponIdAndUserId(anyLong(), anyString())).thenReturn(true);

    // when
    boolean result = systemUnderTest.exists(COUPON, USER_ID);

    // then
    assertThat(result).isTrue();
    verify(jpaCouponUsageRepository).existsByCouponIdAndUserId(COUPON.getId(), USER_ID.id());
  }

  @Test
  void save_givenCouponAndUserIdToSave_shouldConvertItToEntity_thenSaveInRepository() {

    // given
    Instant before = Instant.now();

    // when
    systemUnderTest.save(COUPON, USER_ID);

    // then
    Instant after = Instant.now();
    ArgumentCaptor<CouponUsageEntity> couponEntityArgumentCaptor = ArgumentCaptor.forClass(CouponUsageEntity.class);
    verify(jpaCouponUsageRepository).save(couponEntityArgumentCaptor.capture());

    CouponUsageEntity savedCouponUsage = couponEntityArgumentCaptor.getValue();
    assertThat(savedCouponUsage.getCouponId()).isEqualTo(COUPON.getId());
    assertThat(savedCouponUsage.getUserId()).isEqualTo(USER_ID.id());
    assertThat(savedCouponUsage.getUsedAt()).isBetween(before, after);
  }

  @NonNull
  private static Coupon createCoupon() {
    Coupon coupon = new Coupon(null, null, 0);
    coupon.setId(1L);
    return coupon;
  }
}