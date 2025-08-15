# Use a lightweight Java 20 base image
FROM openjdk:20-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from your target directory into the container
# The JAR is renamed to app.jar for simplicity
COPY target/virtualBookStore-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# The command to run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]