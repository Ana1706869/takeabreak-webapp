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
ENV PORT=10000

# Copiar JAR do stage anterior
COPY --from=builder /build/target/take-a-break-web-1.0.0.jar app.jar

# Expor porta usada pela plataforma (Render usa PORT=10000 por default)
EXPOSE 10000

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD sh -c 'wget --no-verbose --tries=1 --spider "http://localhost:${PORT}/login" || exit 1'

# Executar com variaveis de ambiente
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT} --spring.datasource.url=$DB_URL --spring.datasource.username=$DB_USER --spring.datasource.password=$DB_PASS"]
