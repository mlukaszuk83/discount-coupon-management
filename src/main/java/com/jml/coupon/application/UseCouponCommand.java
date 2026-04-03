package com.jml.coupon.application;

public record UseCouponCommand(String couponCode, String userId) {
}