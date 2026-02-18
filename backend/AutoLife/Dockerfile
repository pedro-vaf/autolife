FROM ubuntu:latest
LABEL authors="pedro"
# ---------- STAGE 1: BUILD ----------
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app
COPY . .

RUN mvn -DskipTests clean package

# ---------- STAGE 2: RUNTIME ----------
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 5440

ENTRYPOINT ["java","-jar","app.jar"]