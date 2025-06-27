FROM bellsoft/liberica-openjdk-alpine:21 AS builder
WORKDIR /builder
COPY . .
RUN --mount=type=cache,target=/root/.gradle \
    chmod +x gradlew && ./gradlew clean build -x test --no-daemon

FROM bellsoft/liberica-openjre-alpine:21 AS layers
WORKDIR /layers
COPY --from=builder /builder/build/libs/*.jar app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --destination extracted

FROM bellsoft/liberica-openjre-alpine:21
WORKDIR /app
RUN addgroup --system appuser && adduser -S -s /usr/sbin/nologin -G appuser appuser
COPY --from=layers /layers/extracted/dependencies/ ./
COPY --from=layers /layers/extracted/spring-boot-loader/ ./
COPY --from=layers /layers/extracted/snapshot-dependencies/ ./
COPY --from=layers /layers/extracted/application/ ./
RUN chown -R appuser:appuser /app
USER appuser
VOLUME /tmp

ENV JAVA_RESERVED_CODE_CACHE_SIZE="240M"
ENV JAVA_MAX_DIRECT_MEMORY_SIZE="10M"
ENV JAVA_MAX_METASPACE_SIZE="179M"
ENV JAVA_XSS="1M"
ENV JAVA_XMX="345M"
ENV JAVA_ERROR_FILE_OPTS="-XX:ErrorFile=/tmp/java_error.log"
ENV JAVA_HEAP_DUMP_OPTS="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp"
ENV JAVA_ON_OUT_OF_MEMORY_OPTS="-XX:+ExitOnOutOfMemoryError"
ENV JAVA_NATIVE_MEMORY_TRACKING_OPTS="-XX:NativeMemoryTracking=summary -XX:+UnlockDiagnosticVMOptions -XX:+PrintNMTStatistics"

HEALTHCHECK --interval=10s --timeout=3s --start-period=10s --retries=5 \
  CMD wget -qO- http://localhost:8081/actuator/health | grep UP || exit 1

ENTRYPOINT java \
    -XX:ReservedCodeCacheSize=$JAVA_RESERVED_CODE_CACHE_SIZE \
    -XX:MaxDirectMemorySize=$JAVA_MAX_DIRECT_MEMORY_SIZE \
    -XX:MaxMetaspaceSize=$JAVA_MAX_METASPACE_SIZE \
    -Xss$JAVA_XSS \
    -Xmx$JAVA_XMX \
    $JAVA_HEAP_DUMP_OPTS \
    $JAVA_ON_OUT_OF_MEMORY_OPTS \
    $JAVA_ERROR_FILE_OPTS \
    $JAVA_NATIVE_MEMORY_TRACKING_OPTS \
    -jar app.jar