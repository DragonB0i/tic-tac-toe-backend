# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set a working directory inside the container
WORKDIR /app

# Copy the Maven wrapper files and build the project
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw

# Copy the rest of your application's source code
COPY src ./src

# --- THIS IS THE FINAL FIX ---
# This command forces a clean build to prevent packaging errors.
RUN ./mvnw clean package -DskipTests

# Render's required port for web services
EXPOSE 10000

# The standard command to run the application
ENTRYPOINT ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
