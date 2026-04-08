# Coupon discount management service

## 1. Service Description

This service provides a simple API to create and use discount coupons.

### Behavior:

- POST requests to `/v1/coupons`:
  - Returns `201 Created` and JSON body containing coupon data if the Coupon has been created successfully.
  - Returns `400 Bad Request` if the coupon code or country code are empty with JSON body as ProblemDetail.
  - Returns `409 Conflict` if the coupon with the same code already exist with JSON body as ProblemDetail.
  - Returns `500 Internal Server Error` in case on any unexpected error with JSON body as ProblemDetail.
- POST requests to `/v1/coupon-usages`:
  - Returns `200 OK` and JSON body containing usage data if the coupon has been successfully used by user.
  - Returns `400 Bad Request` if the coupon code or user id are empty with JSON body as ProblemDetail.
  - Returns `403 Forbidden` if the coupon used by user belongs to other country than the request origin (by IP address
    verified by ip-api.com) with JSON body as ProblemDetail
  - Returns `404 Not Found` if the coupon with given code does not exist with JSON body as ProblemDetail.
  - Returns `409 Conflict` if the coupon was already used by the user or the maximum number of coupon usages has been
    reached, with JSON body as ProblemDetail.
  - Returns `500 Internal Server Error` in case on any unexpected error with JSON body as ProblemDetail.
- GET request to `/actuator/health`:
  - Returns `200 OK` and JSON body containing information about service health, including db and geolocation service
    health
- When application is about to be run locally by dev, the `spring.profiles.active` application.yaml property can be
  changed to `dev` to avoid calling `http://ip-api.com` to validate user country by request id (it would always return
  fail result, because of local 127.0.0.1 address used).

  Other workaround is to add a `X-Forwarded-For` header key with value pointing to valid IP address originating in
  desired country, to the request.

---

## 2. Tech Stack

**Runtime Environment:**

- Java 25

**Frameworks & Libraries:**

- Spring Boot (v4)
  - `spring-boot-starter-webmvc` – for REST API
  - `spring-boot-starter-actuator` – for health checks and monitoring
  - `spring-boot-restclient` – for external HTTP requests
  - `spring-boot-starter-data-jpa` - for db data access
  - `spring-boot-devtools` – for local development
  - `spring-boot-starter-flyway` - for db schema migration
  - `spring-boot-docker-compose` – for optional Docker Compose support
- Lombok (`lombok`) – to reduce boilerplate
- Apache Commons Text (`commons-text`) – for string utilities
- Testing:
  - `spring-boot-starter-webmvc-test`
  - `spring-boot-starter-actuator-test`
  - `spring-boot-restclient-test`
  - `spring-boot-starter-data-jpa-test`
  - `spring-boot-starter-flyway-test`
  - `org.testcontainers.postgresql`

**Build Tool:**

- Maven

**Containerization:**

- Docker (multi-stage build with lightweight Alpine JDK 25)
- Docker Compose support

---

## 3. How to build the service

Make sure you have Maven installed, then:

```
mvn clean package -DskipTests
```

This will create a JAR in `target/`.

---

## 4. How to run automatic tests

Run all unit and integration tests using Maven:

```
mvn test
```

---

## 5. How to run the service locally

- **Option A – Run with Maven**

  Change application property `spring.profiles.active` to `dev` (do not commit this change), then

  ```
  mvn spring-boot:run
  ```

  The service will start on port 8080.


- **Option B – Run with Docker Compose**

  Go to docker folder and run
  ```
  docker-compose up --build
  ```

  This will build the image and start the service on `http://localhost:8080`.

---

## 6. Sample requests

- **Create coupon**

  POST `http://localhost:8080/v1/coupons`

  Content-Type: application/json

  ```json
  {
    "couponCode": "WIOSNA",
    "countryCode": "PL",
    "maxUses": 3
  }
  ```

- **Use coupon**

  POST `http://localhost:8080/v1/coupon-usages`

  Content-Type: application/json

  ```json
  {
    "code": "wiosna",
    "userId": "sample.user@domain.com"
  }
  ```