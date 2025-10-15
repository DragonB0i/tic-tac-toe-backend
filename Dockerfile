FROM openjdk:17-jdk-slim

# Set a working directory inside the container
WORKDIR /app

# Copy the Maven wrapper files and build the project
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw package -DskipTests

# Expose port 8080
EXPOSE 8080

# --- THE FINAL FIX IS HERE ---
# This command manually overrides the settings at startup.
# It uses the environment variables we set on Render.
ENTRYPOINT ["java", \
            "-Dspring.datasource.url=${SPRING_DATASOURCE_URL}", \
            "-Dspring.datasource.username=${SPRING_DATASOURCE_USERNAME}", \
            "-Dspring.datasource.password=${SPRING_DATASOURCE_PASSWORD}", \
            "-Dspring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect", \
            "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
