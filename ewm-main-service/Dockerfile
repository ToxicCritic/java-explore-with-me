FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build
# весь multi‑module проект сразу
COPY . .
# собрать только главный сервис и его зависимые модули (common, client, stats-service)
RUN mvn clean package -pl ewm-main-service -am -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /build/ewm-main-service/target/ewm-main-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]