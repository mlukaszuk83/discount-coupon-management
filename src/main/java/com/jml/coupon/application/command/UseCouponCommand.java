package com.jml.coupon.application.command;

public record UseCouponCommand(String couponCode, String userId) {
}