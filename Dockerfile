# --- STAGE 1: The Build Environment ---
# This stage is like a temporary workshop just for building the .jar file.
FROM openjdk:17-jdk-slim AS build

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw

COPY src ./src

# This command will now correctly build the full, executable .jar file
RUN ./mvnw clean package -DskipTests


# --- STAGE 2: The Final, Runtime Environment ---
# This stage is the clean, final container that will actually run the game.
# THIS IS THE ONE-LINE FIX: We use 'jdk-slim' instead of the non-existent 'jre-slim'
FROM openjdk:17-jdk-slim

WORKDIR /app

# This command copies ONLY the finished .jar file from the 'build' workshop.
COPY --from=build /app/target/*.jar app.jar

# Render requires this port for web services
EXPOSE 10000

# This is the final, simple command to run your game.
ENTRYPOINT ["java", "-jar", "app.jar"]
