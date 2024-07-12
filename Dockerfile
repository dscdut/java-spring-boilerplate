# Step 1: Use Maven to build the project
FROM maven:3.8.4-openjdk-11 AS build

# Set the working directory
WORKDIR /app

# Copy the Maven configuration file
COPY pom.xml .

# Download dependencies before copying the rest of the source code
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Run mvn clean install to build the project
RUN mvn clean install

# Step 2: Use a lightweight JRE image to run the application
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar spring-boot-boilerplate.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "spring-boot-boilerplate.jar"]
