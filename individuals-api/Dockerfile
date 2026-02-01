# STAGE 1: Сборка и подготовка слоёв
FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /app

# Копируем Gradle Wrapper и конфигурацию
COPY gradlew .
COPY gradle ./gradle
COPY individuals-api/build.gradle.kts ./individuals-api/
COPY settings.gradle.kts .

RUN echo "=== Download Gradle dependencies (for caching) ===" && \
    ./gradlew --no-daemon dependencies

# Копируем исходный код
COPY individuals-api ./individuals-api

RUN echo "=== Build executable JAR ===" && \
    ./gradlew --no-daemon :individuals-api:bootJar && \
    echo "=== Extract layers from JAR ===" && \
    java -Djarmode=tools -jar individuals-api/build/libs/individuals-api.jar extract --layers --launcher && \
    echo "=== Delete executable JAR ===" && \
    rm -f individuals-api/build/libs/individuals-api.jar

# STAGE 2: Финальный образ
FROM eclipse-temurin:25-jre-alpine

RUN echo "=== Install curl for healthcheck ===" && \
    apk add --no-cache curl && \
    echo "=== Create unprivileged user ===" && \
    addgroup -g 1001 -S pspuser && \
    adduser -u 1001 -S pspuser -G pspuser

WORKDIR /app

# Копируем слои из builder-stage
COPY --from=builder /app/BOOT-INF/lib ./
COPY --from=builder /app/BOOT-INF/classpath.idx ./
COPY --from=builder /app/org ./

RUN echo "=== Remove unnecessary JARs ===" && \
    find BOOT-INF/lib/ -name "*-sources.jar" -delete && \
    find BOOT-INF/lib/ -name "*-javadoc.jar" -delete && \
    find BOOT-INF/lib/ -name "*-tests.jar" -delete && \
    echo "=== Set app owner ===" && \
    chown -R pspuser:pspuser /app

USER pspuser

EXPOSE 8081

# Run
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]