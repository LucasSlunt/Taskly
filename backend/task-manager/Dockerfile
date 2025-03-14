FROM openjdk:21-jdk-slim

WORKDIR /app

# Copy only the built JAR file, whcih builds on your machine first
COPY target/*.jar app.jar

# Expose the application port, so application inside the container listens to the port 8080
EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}

CMD ["java", "-jar", "app.jar"]
