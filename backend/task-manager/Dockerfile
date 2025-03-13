# Use OpenJDK as base image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy only the built JAR file, whcih builds on your machine first
COPY target/*.jar app.jar

# Expose the application port, so application inside the container listens to the port 8080
EXPOSE 8080

# Set environment variables for profiles, so set when docker-compose.yml is run
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}

# Run the application
CMD ["java", "-jar", "app.jar"]
