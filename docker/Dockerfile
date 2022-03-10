FROM eclipse-temurin:17-jre-alpine

ENV TZ=Europe/Warsaw
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apk add --no-cache tini
ENTRYPOINT ["/sbin/tini", "--"]

WORKDIR /
EXPOSE 8080

COPY todo-app-sample-0.0.1-SNAPSHOT.jar /app.jar

ENV JVM_PARAMS="-Djava.security.egd=file:/dev/./urandom"
CMD ["sh", "-c", "java ${JVM_PARAMS} -jar /app.jar"]