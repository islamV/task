# Use an official OpenJDK 22 runtime as a parent image
FROM openjdk:22-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Compile the Java source code (assuming Cipher.java is the main file)
RUN javac Main.java

# Define the command to run your application (update class name as needed)
CMD ["java", "Cipher"]
