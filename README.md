# CTSE Lab 5 Microservices (IT22036384)

A Spring Boot microservices lab with an API Gateway and three backend services.

## Services
- `api-gateway` (port 8080)
- `item-service` (port 8081)
- `order-service` (port 8082)
- `payment-service` (port 8083)

## Prerequisites
- Java 21
- Maven 3.9+
- Docker + Docker Compose

## Run Locally (without Docker)
```bash
./mvnw -q -DskipTests package

# In separate terminals
cd api-gateway && ./mvnw spring-boot:run
cd item-service && ./mvnw spring-boot:run
cd order-service && ./mvnw spring-boot:run
cd payment-service && ./mvnw spring-boot:run
```

## Docker (Optimized Images)
Each microservice has its own multi-stage Dockerfile:
- Build stage uses `maven:3.9.9-eclipse-temurin-21` to compile and package the app.
- Runtime stage uses the smaller `eclipse-temurin:21-jre-jammy` image to keep images lean.
- Dependencies are cached by copying `pom.xml` files first and running `mvn dependency:go-offline`.
- `.dockerignore` excludes `target/` and IDE folders to keep build context small.

### Build Images (from repo root)
```bash
docker build -f api-gateway/Dockerfile -t api-gateway:latest .
docker build -f item-service/Dockerfile -t item-service:latest .
docker build -f order-service/Dockerfile -t order-service:latest .
docker build -f payment-service/Dockerfile -t payment-service:latest .
```

### Build and Run with Docker Compose
```bash
docker compose up --build
```

### Run with Docker Compose (already built)
```bash
docker compose up
```

## Notes
- The API Gateway routes to services by Docker service name (e.g., `http://item-service:8081`).
- Ports are mapped to the host so you can test locally:
  - `http://localhost:8080` (gateway)
  - `http://localhost:8081` (item)
  - `http://localhost:8082` (order)
  - `http://localhost:8083` (payment)
