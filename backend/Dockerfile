# Multi-stage build for Payara Micro
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage with Payara Micro
FROM payara/micro:6.2024.9-jdk17

# Copy the WAR file from build stage
COPY --from=build /app/target/event-ticket-1.0-SNAPSHOT.war $DEPLOY_DIR

# Expose port
EXPOSE 8080

# Configure Payara Micro
CMD ["--deploy", "/opt/payara/deployments/event-ticket-1.0-SNAPSHOT.war", "--contextroot", "/"] 