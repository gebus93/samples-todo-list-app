FROM eclipse-temurin:17-jre-alpine

ENV TZ=Europe/Warsaw
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apk add --no-cache tini curl
ENTRYPOINT ["/sbin/tini", "--"]

WORKDIR /
EXPOSE 8080

COPY todo-app-sample-0.0.1-SNAPSHOT.jar /app.jar

ENV JVM_PARAMS="-Djava.security.egd=file:/dev/./urandom -server -XX:+UseStringDeduplication -XX:StringDeduplicationAgeThreshold=3 -XX:+OptimizeStringConcat -XX:MinHeapFreeRatio=10 -XX:MaxHeapFreeRatio=70 -XX:CompressedClassSpaceSize=16m -XX:ReservedCodeCacheSize=32m -XX:MetaspaceSize=64M -XX:MaxMetaspaceSize=96M -XX:MinMetaspaceFreeRatio=10 -XX:MaxMetaspaceFreeRatio=90 -Xmx256M -XX:+PrintFlagsFinal"
CMD ["sh", "-c", "java ${JVM_PARAMS} -jar /app.jar"]