# syntax=docker/dockerfile:1
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY item-service/pom.xml item-service/pom.xml
COPY order-service/pom.xml order-service/pom.xml
COPY payment-service/pom.xml payment-service/pom.xml

RUN mvn -q -DskipTests -pl :item-service -am dependency:go-offline

COPY item-service/src item-service/src
RUN mvn -q -DskipTests -pl :item-service -am package

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /workspace/item-service/target/*.jar /app/app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]
