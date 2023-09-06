FROM arm64v8/alpine:latest

RUN apk add --no-cache openjdk17-jre

WORKDIR /app

ENV PORT 9090

EXPOSE 9090

COPY target/Fluxcart-0.0.1-SNAPSHOT.jar /app/Fluxcart-0.0.1-SNAPSHOT.jar

ENTRYPOINT exec java $JAVA_OPTS -jar Fluxcart-0.0.1-SNAPSHOT.jar


