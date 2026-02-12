# STAGE 1: Извлечение слоёв
FROM amazoncorretto:25 AS builder

WORKDIR /project

# Копируем готовый JAR (собранный вне Docker заранее)
COPY individuals-api/build/libs/individuals-api.jar /project/app.jar

RUN echo "=== Extract layers from JAR ===" && \
    java -Djarmode=tools -jar app.jar extract --layers --launcher && \
    echo "=== Delete executable JAR ===" && \
    rm -f app.jar

# STAGE 2: Финальный образ
FROM amazoncorretto:25

RUN echo "=== Install curl for healthcheck + other utils ===" && \
    yum install -y --allowerasing curl shadow-utils findutils && \
    echo "=== Clear 'yum' package manager cache to reduce image size ===" && \
    yum clean all && \
    echo "=== Create unprivileged user ===" && \
    groupadd -g 1001 pspuser && \
    useradd -u 1001 -g pspuser -m -s /bin/false pspuser

WORKDIR /app

# Копируем слои из builder-stage
COPY --from=builder /project/app/dependencies/ ./
COPY --from=builder /project/app/spring-boot-loader/ ./
COPY --from=builder /project/app/snapshot-dependencies/ ./
COPY --from=builder /project/app/application/ ./

RUN echo "=== Remove unnecessary JARs ===" && \
    find BOOT-INF/lib/ -name "*-sources.jar" -delete && \
    find BOOT-INF/lib/ -name "*-javadoc.jar" -delete && \
    find BOOT-INF/lib/ -name "*-tests.jar" -delete && \
    chown -R pspuser:pspuser /app

USER pspuser

EXPOSE 8081

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "org.springframework.boot.loader.launch.JarLauncher"]