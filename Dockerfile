FROM openjdk:17-jdk-slim

WORKDIR /app

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Copy source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/portal-vagas-0.0.1-SNAPSHOT.jar"]