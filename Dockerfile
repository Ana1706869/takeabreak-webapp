# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /build
COPY pom.xml .
COPY src ./src
RUN mvn -DskipTests clean package spring-boot:repackage

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar JAR do stage anterior
COPY --from=builder /build/target/take-a-break-web-1.0.0.jar app.jar

# Expor porta padrão da aplicação
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD sh -c 'wget --no-verbose --tries=1 --spider "http://localhost:${PORT:-8081}/login" || exit 1'

# Executar com configuração resolvida pelo Spring via environment/application.properties
ENTRYPOINT ["java", "-jar", "app.jar"]
