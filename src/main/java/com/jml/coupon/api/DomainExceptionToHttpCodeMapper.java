package com.jml.coupon.api;

import com.jml.coupon.domain.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
class DomainExceptionToHttpCodeMapper {

  HttpStatus toStatus(DomainException e) {

    switch (e.getCode()) {
      case COUPON_USED, COUPON_LIMIT_REACHED, COUPON_EXISTS -> {
        return HttpStatus.CONFLICT;
      }
      case COUNTRY_INVALID -> {
        return HttpStatus.FORBIDDEN;
      }
      case COUPON_NOT_FOUND -> {
        return HttpStatus.NOT_FOUND;
      }
      default -> {
        return HttpStatus.BAD_REQUEST;
      }
    }
  }
}