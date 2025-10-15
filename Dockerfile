# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set a working directory inside the container
WORKDIR /app

# Copy the Maven wrapper files and build the project
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw
RUN ./mvnw package -DskipTests

# Expose port 8080
EXPOSE 8080


ENTRYPOINT ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
