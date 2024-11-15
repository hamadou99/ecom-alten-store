# Stage 1: Build
FROM maven:3.8.1-openjdk-17-slim AS build
WORKDIR /opt/app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY ./src ./src
RUN mvn clean install -Djar.demo -Dmaven.test.skip=true

# Stage 2: Runtime
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /opt/app/target/ecom-0.0.1-SNAPSHOT.jar /app/ecom-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "ecom-0.0.1-SNAPSHOT.jar"]

