package com.jml.coupon.api.exception;

import com.jml.coupon.domain.exception.DomainException;
import com.jml.coupon.domain.exception.DomainExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final DomainExceptionToHttpCodeMapper domainExceptionToHttpCodeMapper;

  @ExceptionHandler(DomainException.class)
  ProblemDetail handleDomainException(DomainException e, HttpServletRequest request) {

    final HttpStatus status = domainExceptionToHttpCodeMapper.toStatus(e);
    final DomainExceptionCode code = e.getCode();
    final ProblemDetail problem = ProblemDetail.forStatus(status);
    problem.setTitle(code.name());
    problem.setDetail(e.getMessage());
    problem.setType(createType(code.getType()));
    problem.setInstance(URI.create(request.getRequestURI()));
    return problem;
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleGenericException(Exception e, HttpServletRequest request) {

    final ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    problem.setTitle("Internal server error");
    problem.setDetail("Unexpected error occurred");
    problem.setType(createType("internal"));
    problem.setInstance(URI.create(request.getRequestURI()));
    return problem;
  }

  private URI createType(String name) {
    return URI.create("https://api.coupons.com/errors/" + name);
  }
}