FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw
COPY src ./src
RUN ./mvnw clean package -DskipTests

# --- STAGE 2: The Final, Runtime Environment ---
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render requires this port for web services
EXPOSE 10000

# --- THE HARDCODED FIX ---
# We are manually writing your database credentials here.
# REPLACE the placeholders below with your actual values.
ENTRYPOINT ["java", \
            "-Dspring.datasource.url=jdbc:postgresql://YOUR_HOST_HERE/YOUR_DATABASE_NAME_HERE", \
            "-Dspring.datasource.username=YOUR_USERNAME_HERE", \
            "-Dspring.datasource.password=YOUR_PASSWORD_HERE", \
            "-Dspring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect", \
            "-jar", \
            "app.jar"]
