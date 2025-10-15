# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set a working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and project file
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Give it permission to run
RUN chmod +x ./mvnw

# Copy the rest of your application's source code
COPY src ./src

# Build the application using the default 'package' goal.
# This is the standard command that should create the executable jar.
RUN ./mvnw package -DskipTests

# Render's required port for web services
EXPOSE 10000

# The standard command to run the application
ENTRYPOINT ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
