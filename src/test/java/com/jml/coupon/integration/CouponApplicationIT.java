package com.jml.coupon.integration;

import com.jml.coupon.CouponApplication;
import com.jml.coupon.TestConfig;
import com.jml.coupon.application.dto.CouponDto;
import com.jml.coupon.application.dto.CouponUsageDto;
import com.jml.coupon.application.request.CreateCouponRequest;
import com.jml.coupon.application.request.UseCouponRequest;
import com.jml.coupon.infrastructure.persistence.springdata.JpaCouponRepository;
import com.jml.coupon.infrastructure.persistence.springdata.JpaCouponUsageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = CouponApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig.class)
@ActiveProfiles("test")
class CouponApplicationIT {

  @LocalServerPort
  private int port;

  @Container
  private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:latest");
  @Autowired
  private JpaCouponRepository jpaCouponRepository;

  @DynamicPropertySource
  private static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);
  }

  private final TestRestTemplate restTemplate = new TestRestTemplate();

  @Autowired
  private JpaCouponRepository couponRepository;

  @Autowired
  private JpaCouponUsageRepository couponUsageRepository;

  @Test
  void shouldCreateNewCoupon() {

    // given
    String couponCode = "code-1";
    String countryCode = "PL";
    int maxUses = 2;
    CreateCouponRequest request = new CreateCouponRequest(couponCode, countryCode, maxUses);
    HttpEntity<CreateCouponRequest> entity = new HttpEntity<>(request, new HttpHeaders());

    // when
    ResponseEntity<CouponDto> result = restTemplate.exchange(createURL("/v1/coupons"), HttpMethod.POST, entity, CouponDto.class);

    // then
    CouponDto couponDto = result.getBody();
    assertThat(couponDto).isNotNull();
    assertThat(couponDto.id()).isGreaterThan(0);
    assertThat(couponDto.code()).isEqualTo(couponCode);
    assertThat(couponDto.country()).isEqualTo(countryCode);
    assertThat(couponDto.maxUses()).isEqualTo(maxUses);
    assertThat(couponDto.currentUses()).isZero();
    assertThat(couponDto.createdAt()).isNotNull();

    assertThat(jpaCouponRepository.findById(couponDto.id())).isPresent();
  }

  @Test
  void shouldUseExistingCoupon() {

    // given
    String couponCode = "code-2";
    CreateCouponRequest createRequest = new CreateCouponRequest(couponCode, "PL", 2);
    HttpEntity<CreateCouponRequest> createEntity = new HttpEntity<>(createRequest, new HttpHeaders());
    restTemplate.exchange(createURL("/v1/coupons"), HttpMethod.POST, createEntity, CouponDto.class);

    String userId = "user-id";
    UseCouponRequest useCouponRequest = new UseCouponRequest(couponCode, userId);
    HttpEntity<UseCouponRequest> useEntity = new HttpEntity<>(useCouponRequest, new HttpHeaders());

    // when
    ResponseEntity<CouponUsageDto> result = restTemplate.exchange(createURL("/v1/coupon-usages"), HttpMethod.POST, useEntity, CouponUsageDto.class);

    // then
    CouponUsageDto couponUsageDto = result.getBody();
    assertThat(couponUsageDto).isNotNull();
    assertThat(couponUsageDto.id()).isGreaterThan(0);
    assertThat(couponUsageDto.code()).isEqualTo(couponCode);
    assertThat(couponUsageDto.userId()).isEqualTo(userId);
    assertThat(couponUsageDto.usedAt()).isNotNull();

    assertThat(couponUsageRepository.findById(couponUsageDto.id())).isPresent();
  }

  private String createURL(String uri) {
    return "http://localhost:" + port + uri;
  }
}