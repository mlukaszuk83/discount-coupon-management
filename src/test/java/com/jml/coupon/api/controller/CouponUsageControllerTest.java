package com.jml.coupon.api.controller;

import com.jml.coupon.TestConfig;
import com.jml.coupon.api.exception.DomainExceptionToHttpCodeMapper;
import com.jml.coupon.application.handler.UseCouponHandler;
import com.jml.coupon.domain.CouponRepository;
import com.jml.coupon.domain.CouponUsageRepository;
import com.jml.coupon.domain.model.Country;
import com.jml.coupon.domain.model.Coupon;
import com.jml.coupon.domain.model.CouponCode;
import com.jml.coupon.domain.model.CouponUsage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CouponUsageController.class)
@Import({UseCouponHandler.class, DomainExceptionToHttpCodeMapper.class, TestConfig.class})
class CouponUsageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CouponRepository couponRepository;

  @MockitoBean
  private CouponUsageRepository couponUsageRepository;

  @Test
  void use_givenRequestWithExistingCouponCodeValidForGivenCountryANdNotUsedByUser_shouldUseTheCouponAndReturnABodyWith200StatusResponse() throws Exception {

    // given
    String body = """
        {
          "code": "code-1",
          "userId": "user-1"
        }
        """;

    Coupon coupon = new Coupon(new CouponCode("code-1"), new Country("PL"), 4);
    when(couponRepository.findByCode(any(CouponCode.class))).thenReturn(Optional.of(coupon));
    when(couponUsageRepository.exists(any(CouponUsage.class))).thenReturn(false);
    when(couponRepository.incrementIfAvailable(any(CouponCode.class))).thenReturn(true);
    when(couponUsageRepository.save(any(CouponUsage.class))).thenReturn(1L);

    // when / then
    mockMvc.perform(post("/v1/coupon-usages").contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.code").value("code-1"))
        .andExpect(jsonPath("$.userId").value("user-1"))
        .andExpect(jsonPath("$.usedAt").exists());
  }

  @Test
  void use_givenRequestWithNonExistingCouponCode_shouldReturnA404StatusResponse() throws Exception {

    // given
    String body = """
        {
          "code": "code-2",
          "userId": "user-1"
        }
        """;

    when(couponRepository.findByCode(any(CouponCode.class))).thenReturn(Optional.empty());

    // when / then
    mockMvc.perform(post("/v1/coupon-usages").contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.detail").value("Coupon with given code was not found"))
        .andExpect(jsonPath("$.instance").value("/v1/coupon-usages"))
        .andExpect(jsonPath("$.status").value("404"))
        .andExpect(jsonPath("$.title").value("COUPON_NOT_FOUND"))
        .andExpect(jsonPath("$.type").value("https://api.coupons.com/errors/coupon-not-found"));
  }

  @Test
  void use_givenRequestWithEmptyCouponCode_shouldReturnA400StatusResponse() throws Exception {

    // given
    String body = """
        {
          "code": "",
          "userId": "user-1"
        }
        """;

    when(couponRepository.findByCode(any(CouponCode.class))).thenReturn(Optional.empty());

    // when / then
    mockMvc.perform(post("/v1/coupon-usages").contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.detail").value("Coupon code cannot be empty"))
        .andExpect(jsonPath("$.instance").value("/v1/coupon-usages"))
        .andExpect(jsonPath("$.status").value("400"))
        .andExpect(jsonPath("$.title").value("COUPON_CODE_EMPTY"))
        .andExpect(jsonPath("$.type").value("https://api.coupons.com/errors/empty-coupon-code"));
  }

  @Test
  void use_givenRequestWithInvalidCountryCode_shouldReturnA403StatusResponse() throws Exception {

    // given
    String body = """
        {
          "code": "code-3",
          "userId": "user-1"
        }
        """;

    Coupon coupon = new Coupon(new CouponCode("code-3"), new Country("EN"), 4);
    when(couponRepository.findByCode(any(CouponCode.class))).thenReturn(Optional.of(coupon));

    // when / then
    mockMvc.perform(post("/v1/coupon-usages").contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isForbidden())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.detail").value("The coupon cannot be used in this country"))
        .andExpect(jsonPath("$.instance").value("/v1/coupon-usages"))
        .andExpect(jsonPath("$.status").value("403"))
        .andExpect(jsonPath("$.title").value("COUNTRY_INVALID"))
        .andExpect(jsonPath("$.type").value("https://api.coupons.com/errors/invalid-country"));
  }

  @Test
  void use_givenRequestWithEmptyUserId_shouldReturnA400StatusResponse() throws Exception {

    // given
    String body = """
        {
          "code": "code-3",
          "userId": ""
        }
        """;

    Coupon coupon = new Coupon(new CouponCode("code-3"), new Country("PL"), 4);
    when(couponRepository.findByCode(any(CouponCode.class))).thenReturn(Optional.of(coupon));

    // when / then
    mockMvc.perform(post("/v1/coupon-usages").contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.detail").value("User id cannot be empty"))
        .andExpect(jsonPath("$.instance").value("/v1/coupon-usages"))
        .andExpect(jsonPath("$.status").value("400"))
        .andExpect(jsonPath("$.title").value("USER_EMPTY"))
        .andExpect(jsonPath("$.type").value("https://api.coupons.com/errors/empty-user-id"));
  }

  @Test
  void use_givenRequestWithUserIdWhichAlreadyUsedTheCoupon_shouldReturnA409StatusResponse() throws Exception {

    // given
    String body = """
        {
          "code": "code-4",
          "userId": "user-2"
        }
        """;

    Coupon coupon = new Coupon(new CouponCode("code-4"), new Country("PL"), 4);
    when(couponRepository.findByCode(any(CouponCode.class))).thenReturn(Optional.of(coupon));
    when(couponUsageRepository.exists(any(CouponUsage.class))).thenReturn(true);

    // when / then
    mockMvc.perform(post("/v1/coupon-usages").contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.detail").value("Coupon with given code was already used by given user"))
        .andExpect(jsonPath("$.instance").value("/v1/coupon-usages"))
        .andExpect(jsonPath("$.status").value("409"))
        .andExpect(jsonPath("$.title").value("COUPON_USED"))
        .andExpect(jsonPath("$.type").value("https://api.coupons.com/errors/coupon-already-used"));
  }

  @Test
  void use_givenUserTriesToUseTheCouponTwiceInASameTime_theDataIntegrityExceptionIsThrown_thenShouldReturnA409StatusResponse() throws Exception {

    // given
    String body = """
        {
          "code": "code-4",
          "userId": "user-2"
        }
        """;

    Coupon coupon = new Coupon(new CouponCode("code-4"), new Country("PL"), 4);
    when(couponRepository.findByCode(any(CouponCode.class))).thenReturn(Optional.of(coupon));
    when(couponUsageRepository.exists(any(CouponUsage.class))).thenReturn(false);
    when(couponRepository.incrementIfAvailable(any(CouponCode.class))).thenReturn(true);
    when(couponUsageRepository.save(any(CouponUsage.class))).thenThrow(DataIntegrityViolationException.class);

    // when / then
    mockMvc.perform(post("/v1/coupon-usages").contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.detail").value("Coupon with given code was already used by given user"))
        .andExpect(jsonPath("$.instance").value("/v1/coupon-usages"))
        .andExpect(jsonPath("$.status").value("409"))
        .andExpect(jsonPath("$.title").value("COUPON_USED"))
        .andExpect(jsonPath("$.type").value("https://api.coupons.com/errors/coupon-already-used"));
  }

  @Test
  void use_givenRequestWithCouponWhichHasReachedHisUseLimit_shouldReturnA409StatusResponse() throws Exception {

    // given
    String body = """
        {
          "code": "code-5",
          "userId": "user-2"
        }
        """;

    Coupon coupon = new Coupon(new CouponCode("code-4"), new Country("PL"), 4);
    when(couponRepository.findByCode(any(CouponCode.class))).thenReturn(Optional.of(coupon));
    when(couponUsageRepository.exists(any(CouponUsage.class))).thenReturn(false);
    when(couponRepository.incrementIfAvailable(any(CouponCode.class))).thenReturn(false);

    // when / then
    mockMvc.perform(post("/v1/coupon-usages").contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.detail").value("This coupon can't be used anymore, the usage limit has been reached"))
        .andExpect(jsonPath("$.instance").value("/v1/coupon-usages"))
        .andExpect(jsonPath("$.status").value("409"))
        .andExpect(jsonPath("$.title").value("COUPON_LIMIT_REACHED"))
        .andExpect(jsonPath("$.type").value("https://api.coupons.com/errors/coupon-limit-reached"));
  }
}