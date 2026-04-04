package com.jml.coupon.infrastructure.persistence.springdata;

import com.jml.coupon.infrastructure.persistence.entity.CouponEntity;
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
class JpaCouponRepositoryTest {

  @Container
  private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:latest").withDatabaseName("test-db")
      .withUsername("test")
      .withPassword("test");

  @DynamicPropertySource
  private static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);
  }

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private JpaCouponRepository systemUnderTest;

  @Test
  void increment_givenCurrentUsesBelowMaxUses_thenShouldIncrementCurrentUsesBy1() {

    // given
    String code = "code-to-increment";

    CouponEntity entity = new CouponEntity();
    entity.setCode(code);
    entity.setMaxUses(3);
    entity.setCurrentUses(1);
    entity.setCreatedAt(Instant.now());
    entity.setCountry("country");

    entityManager.persistAndFlush(entity);

    // when
    int result = systemUnderTest.increment(code);

    // then
    assertThat(result).isOne();

    entityManager.clear();
    CouponEntity updatedEntity = entityManager.find(CouponEntity.class, entity.getId());
    assertThat(updatedEntity).isNotNull()
        .extracting(CouponEntity::getCurrentUses)
        .isEqualTo(2);
  }

  @Test
  void increment_givenCurrentUsesEqualMaxUses_thenShouldNotIncrementCurrentUsesBy1() {

    // given
    String code = "code-not-to-increment";

    CouponEntity entity = new CouponEntity();
    entity.setCode(code);
    entity.setMaxUses(3);
    entity.setCurrentUses(3);
    entity.setCreatedAt(Instant.now());
    entity.setCountry("country");

    entityManager.persistAndFlush(entity);

    // when
    int result = systemUnderTest.increment(code);

    // then
    assertThat(result).isZero();

    entityManager.clear();
    CouponEntity updatedEntity = entityManager.find(CouponEntity.class, entity.getId());
    assertThat(updatedEntity).isNotNull()
        .extracting(CouponEntity::getCurrentUses)
        .isEqualTo(3);
  }

  @Test
  void save_givenACoupon_whenAttemptToCreateANewOneWithSameCodeIsDone_thenShouldFail() {

    // given
    String code = "duplicate-attempt";
    CouponEntity existingEntity = new CouponEntity();
    existingEntity.setCode(code);
    existingEntity.setMaxUses(1);
    existingEntity.setCurrentUses(0);
    existingEntity.setCreatedAt(Instant.now());
    existingEntity.setCountry("country");

    entityManager.persistAndFlush(existingEntity);

    CouponEntity duplicateEntity = new CouponEntity();
    duplicateEntity.setCode(code);
    duplicateEntity.setMaxUses(14);
    duplicateEntity.setCurrentUses(0);
    duplicateEntity.setCreatedAt(Instant.now());
    duplicateEntity.setCountry("country");

    // when / then
    assertThatExceptionOfType(DataIntegrityViolationException.class).isThrownBy(() -> systemUnderTest.save(duplicateEntity));
  }
}