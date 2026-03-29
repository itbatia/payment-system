# STAGE 1: Extract layers
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /project

# Copy pre-compiled JAR
COPY individuals-api/build/libs/individuals-api.jar /project/app.jar

# Extract layers from JAR and delete it
RUN java -Djarmode=tools -jar app.jar extract --layers --launcher && \
    rm -f app.jar

# STAGE 2: Final image
FROM eclipse-temurin:25-jre

# Install curl for healthcheck, then clear apt cache to reduce image size
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy layers from builder-stage
COPY --from=builder /project/app/dependencies/ ./
COPY --from=builder /project/app/spring-boot-loader/ ./
COPY --from=builder /project/app/snapshot-dependencies/ ./
COPY --from=builder /project/app/application/ ./

EXPOSE 8082

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
