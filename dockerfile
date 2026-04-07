FROM maven:3.9.14-eclipse-temurin-25-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM amd64/eclipse-temurin:25-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/coupon-0.0.1-SNAPSHOT.jar ./coupon-service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","coupon-service.jar"]