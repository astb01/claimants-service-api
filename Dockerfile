FROM openjdk:8-alpine
LABEL org.label-schema.schema-version="1.0" \
      org.label-schema.build-date="unknown" \
      org.label-schema.version="unknown" \
      org.label-schema.name="claimants-service-api" \
      org.label-schema.description="openjdk alpine container with a spring claimants api." \
      maintainer="Assad Ahmad"

WORKDIR /usr/app/claimants-service-api

COPY target/claimants-service*.jar app.jar

EXPOSE 8080

RUN sh -c 'touch app.jar'

ENTRYPOINT [ "java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar" ]
