package com.jml.coupon.infrastructure.persistence.springdata;

import com.jml.coupon.infrastructure.persistence.entity.CouponEntity;
import com.jml.coupon.infrastructure.persistence.entity.CouponUsageEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaCouponUsageRepositoryTest {

  @Container
  private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:latest");

  @DynamicPropertySource
  private static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);
  }

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private JpaCouponUsageRepository systemUnderTest;

  @Test
  void save_givenACouponUsage_whenAttemptToCreateANewOneWithSameCouponIdAndUserIdIsDone_thenShouldFail() {

    // given
    CouponEntity entity = new CouponEntity();
    entity.setCode("code");
    entity.setMaxUses(3);
    entity.setCurrentUses(1);
    entity.setCreatedAt(Instant.now());
    entity.setCountry("country");

    entityManager.persistAndFlush(entity);
    entityManager.refresh(entity);

    String userId = "duplicate-attempt";
    long couponId = entity.getId();

    CouponUsageEntity existingEntity = new CouponUsageEntity();
    existingEntity.setCouponId(couponId);
    existingEntity.setUserId(userId);
    existingEntity.setUsedAt(Instant.now());

    entityManager.persistAndFlush(existingEntity);

    CouponUsageEntity duplicateEntity = new CouponUsageEntity();
    duplicateEntity.setCouponId(couponId);
    duplicateEntity.setUserId(userId);
    duplicateEntity.setUsedAt(Instant.now());

    // when / then
    assertThatExceptionOfType(DataIntegrityViolationException.class).isThrownBy(() -> systemUnderTest.save(duplicateEntity));
  }

  @Test
  void save_givenACouponUsage_whenAttemptToCreateANewOneWithSameCouponIdAndDifferentUserIdIsDone_thenShouldPass() {

    // given
    CouponEntity entity = new CouponEntity();
    entity.setCode("code-2");
    entity.setMaxUses(3);
    entity.setCurrentUses(1);
    entity.setCreatedAt(Instant.now());
    entity.setCountry("country");

    entityManager.persistAndFlush(entity);
    entityManager.refresh(entity);

    String userId = "user-id-1";
    long couponId = entity.getId();

    CouponUsageEntity existingEntity = new CouponUsageEntity();
    existingEntity.setCouponId(couponId);
    existingEntity.setUserId(userId);
    existingEntity.setUsedAt(Instant.now());

    entityManager.persistAndFlush(existingEntity);

    CouponUsageEntity newEntity = new CouponUsageEntity();
    newEntity.setCouponId(couponId);
    newEntity.setUserId("user-id-2");
    newEntity.setUsedAt(Instant.now());

    // when
    CouponUsageEntity result = systemUnderTest.save(newEntity);

    // then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getCouponId()).isEqualTo(couponId);
    assertThat(result.getUserId()).isEqualTo(newEntity.getUserId());
    assertThat(result.getUsedAt()).isEqualTo(newEntity.getUsedAt());
  }
}