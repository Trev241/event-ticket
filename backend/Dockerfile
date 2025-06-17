# Multi-stage build for WildFly
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM quay.io/wildfly/wildfly:28.0.0.Final-jdk20 AS runtime

COPY --from=build /app/target/event-ticket-1.0-SNAPSHOT.war /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080 9990

WORKDIR /app

COPY create-queue.cli .
COPY entrypoint.sh .

CMD ["./entrypoint.sh"]