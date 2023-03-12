# syntax=docker/dockerfile:1
FROM eclipse-temurin:17-jdk-jammy
ADD target/riskyForest-api.jar riskyForest-api.jar
ENTRYPOINT ["java", "-jar","riskyForest-api.jar"]
EXPOSE 8860