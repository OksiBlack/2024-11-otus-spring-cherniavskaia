# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set the working directory
WORKDIR /app

# Copy the Maven POM file and the source code
COPY pom.xml .
COPY ./src ./src

# Package the application (skip tests for faster builds)
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:21-jdk-slim

# Install required libraries for native support
RUN apt-get update && apt-get install -y \
    libtcnative-1 \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Create the application directory
WORKDIR /app

# Copy the packaged JAR from the build stage to the runtime image
COPY --from=build /app/target/hw17-docker-0.0.1-SNAPSHOT.jar /app/bookstore.jar

# Expose the ports
EXPOSE 9090
EXPOSE 9091

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "/app/bookstore.jar"]

