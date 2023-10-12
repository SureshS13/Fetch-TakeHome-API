# Use OpenJDK 11 as the base image
FROM openjdk:11

ARG JAR_FILE=target/*.jar
COPY ./target/fetch-api-demo-0.0.1-SNAPSHOT.jar  app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
