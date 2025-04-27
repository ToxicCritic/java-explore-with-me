FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build
COPY . .

RUN mvn -q dependency:go-offline

RUN mvn -q clean package -pl ewm-main-service -am -DskipTests

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

COPY --from=builder /build/ewm-main-service/target/ewm-main-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]