# Global ARG defined before stages to be accessible across stages
ARG MODULE_NAME

# Stage 1: Gleaner (Collects all pom.xml files while preserving structure)
FROM alpine:3.20 AS gleaner
WORKDIR /app
COPY . .
# Find all pom.xml files and bundle them into a tarball to preserve directory structure
RUN find . -name "pom.xml" | xargs tar -cf /app/poms.tar

# Stage 2: Build
FROM maven:3.9.9-eclipse-temurin-23-alpine AS build
ARG MODULE_NAME
WORKDIR /app

# Extract only the pom.xml files from the gleaner stage
COPY --from=gleaner /app/poms.tar .
RUN tar -xf poms.tar && rm poms.tar

# Resolve dependencies for the specific project and its local dependencies
# This layer is now cached and only rebuilds if any pom.xml in the project changes
RUN mvn dependency:go-offline -B -pl ${MODULE_NAME} -am

# Copy all source code for the actual build
COPY . .
RUN mvn clean package -DskipTests -pl ${MODULE_NAME} -am

# Stage 3: Runtime
FROM eclipse-temurin:23-jre-alpine
ARG MODULE_NAME
WORKDIR /app
RUN addgroup -S cryptotrader && adduser -S cryptotrader -G cryptotrader
USER cryptotrader

# Copy the specific JAR from the build stage using the ARG
COPY --from=build /app/${MODULE_NAME}/target/*.jar app.jar

# Standard JVM optimizations for containers
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
