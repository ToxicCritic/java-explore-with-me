FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /build
COPY . .

RUN mvn -q dependency:go-offline

RUN mvn -q clean package -pl ewm-stats-service -am -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /build/ewm-stats-service/target/ewm-stats-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","app.jar"]