## Build stage
FROM maven:3.9.8-eclipse-temurin-17 AS builder

WORKDIR /workspace
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -U -B -DskipTests dependency:go-offline
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -B -DskipTests clean package

## Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /workspace/target/portal-vagas-0.0.1-SNAPSHOT.jar app.jar

# utilitários para healthcheck em dev
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
