package com.jml.coupon.api.exception;

import com.jml.coupon.domain.exception.CouponAlreadyUsedException;
import com.jml.coupon.domain.exception.DomainException;
import com.jml.coupon.domain.exception.DomainExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  @Mock
  private DomainExceptionToHttpCodeMapper domainExceptionToHttpCodeMapper;

  @InjectMocks
  private GlobalExceptionHandler systemUnderTest;

  @Test
  void handleDomainException_shouldConvertDomainExceptionToProblemDetail() throws URISyntaxException {

    // given
    DomainException exception = new CouponAlreadyUsedException();
    DomainExceptionCode code = exception.getCode();
    HttpStatus httpStatus = HttpStatus.CONFLICT;
    when(domainExceptionToHttpCodeMapper.toStatus(exception)).thenReturn(httpStatus);

    String requestUri = "request-uri";
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getRequestURI()).thenReturn(requestUri);

    String expectedType = "https://api.coupons.com/errors/" + code.getType();

    // when
    ProblemDetail result = systemUnderTest.handleDomainException(exception, request);

    // then
    assertThat(result).extracting(ProblemDetail::getTitle, ProblemDetail::getDetail, ProblemDetail::getType, ProblemDetail::getInstance)
        .containsExactly(code.name(), exception.getMessage(), new URI(expectedType), new URI(requestUri));
  }

  @Test
  void handleGenericException_shouldConvertExceptionToProblemDetail() throws URISyntaxException {

    // given
    Exception exception = new Exception();
    String requestUri = "request-uri";
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getRequestURI()).thenReturn(requestUri);

    String expectedType = "https://api.coupons.com/errors/internal";

    // when
    ProblemDetail result = systemUnderTest.handleGenericException(exception, request);

    // then
    assertThat(result).extracting(ProblemDetail::getTitle, ProblemDetail::getDetail, ProblemDetail::getType, ProblemDetail::getInstance)
        .containsExactly("Internal server error", "Unexpected error occurred", new URI(expectedType), new URI(requestUri));
  }
}