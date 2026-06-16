# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -DskipTests clean package spring-boot:repackage

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Default to production profile in container runtime.
ENV SPRING_PROFILES_ACTIVE=prod

# Copiar JAR do stage anterior
COPY --from=builder /build/target/take-a-break-web-1.0.0.jar app.jar

# Expor porta 8081 (interna)
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8081/login || exit 1

# Executar com variaveis de ambiente
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=8081 --spring.datasource.url=$DB_URL --spring.datasource.username=$DB_USER --spring.datasource.password=$DB_PASS"]
