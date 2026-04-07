package com.jml.coupon.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record CouponDto(Long id, String code, String country, int maxUses, int currentUses,
                        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC") Instant createdAt) {

}