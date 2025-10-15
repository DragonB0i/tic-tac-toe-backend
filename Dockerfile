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

# Build the standard jar file
RUN ./mvnw clean package -DskipTests

# --- THIS IS THE LAST RESORT ---
# Explicitly force the spring-boot plugin to repackage the jar correctly
RUN ./mvnw spring-boot:repackage

# Render's required port for web services
EXPOSE 10000

# The standard command to run the application
ENTRYPOINT ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
