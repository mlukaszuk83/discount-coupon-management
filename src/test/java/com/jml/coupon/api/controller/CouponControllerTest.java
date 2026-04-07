package com.jml.coupon.api.controller;

import com.jml.coupon.api.exception.DomainExceptionToHttpCodeMapper;
import com.jml.coupon.application.handler.CreateCouponHandler;
import com.jml.coupon.domain.CouponRepository;
import com.jml.coupon.domain.model.Coupon;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CouponController.class)
@Import({CreateCouponHandler.class, DomainExceptionToHttpCodeMapper.class})
class CouponControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CouponRepository couponRepository;

  @Test
  void create_givenRequestWithNonExistingCouponCode_shouldCreateItAndReturnABodyWith201StatusResponse() throws Exception {

    // given
    String body = """
        {
          "couponCode": "code-1",
          "countryCode": "PL",
          "maxUses": 5
        }
        """;

    when(couponRepository.save(any(Coupon.class))).thenReturn(1L);

    // when / then
    mockMvc.perform(post("/v1/coupons").contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.code").value("code-1"))
        .andExpect(jsonPath("$.country").value("PL"))
        .andExpect(jsonPath("$.maxUses").value("5"))
        .andExpect(jsonPath("$.currentUses").value("0"))
        .andExpect(jsonPath("$.createdAt").exists());
  }

  @Test
  void create_givenRequestWithExistingCouponCode_shouldReturnA409StatusResponse() throws Exception {

    // given
    String body = """
        {
          "couponCode": "code-2",
          "countryCode": "PL",
          "maxUses": 5
        }
        """;

    when(couponRepository.save(any(Coupon.class))).thenThrow(DataIntegrityViolationException.class);

    // when / then
    mockMvc.perform(post("/v1/coupons").contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isConflict())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.detail").value("A coupon with given code already exists"))
        .andExpect(jsonPath("$.instance").value("/v1/coupons"))
        .andExpect(jsonPath("$.status").value("409"))
        .andExpect(jsonPath("$.title").value("COUPON_EXISTS"))
        .andExpect(jsonPath("$.type").value("https://api.coupons.com/errors/coupon-already-exists"));
  }

  @Test
  void create_givenRequestEmptyCouponCode_shouldReturnA400StatusResponse() throws Exception {

    // given
    String body = """
        {
          "couponCode": "",
          "countryCode": "PL",
          "maxUses": 5
        }
        """;

    when(couponRepository.save(any(Coupon.class))).thenThrow(DataIntegrityViolationException.class);

    // when / then
    mockMvc.perform(post("/v1/coupons").contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.detail").value("Coupon code cannot be empty"))
        .andExpect(jsonPath("$.instance").value("/v1/coupons"))
        .andExpect(jsonPath("$.status").value("400"))
        .andExpect(jsonPath("$.title").value("COUPON_CODE_EMPTY"))
        .andExpect(jsonPath("$.type").value("https://api.coupons.com/errors/empty-coupon-code"));
  }

  @Test
  void create_givenRequestWithEmptyCountryCode_shouldReturnA400StatusResponse() throws Exception {

    // given
    String body = """
        {
          "couponCode": "code-2",
          "countryCode": "",
          "maxUses": 5
        }
        """;

    when(couponRepository.save(any(Coupon.class))).thenThrow(DataIntegrityViolationException.class);

    // when / then
    mockMvc.perform(post("/v1/coupons").contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.detail").value("Country code cannot be empty"))
        .andExpect(jsonPath("$.instance").value("/v1/coupons"))
        .andExpect(jsonPath("$.status").value("400"))
        .andExpect(jsonPath("$.title").value("COUNTRY_EMPTY"))
        .andExpect(jsonPath("$.type").value("https://api.coupons.com/errors/empty-country-code"));
  }
}