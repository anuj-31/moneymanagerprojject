#FROM eclipse-temurin:21-jre
#WORKDIR /app
#COPY target/moneymanager-0.0.1-SNAPSHOT.jar moneymanager-v1.0.jar
#EXPOSE 9090
#ENTRYPOINT ["java","-jar","moneymanager-v1.0.jar"]


# Stage 1: Build the JAR
FROM maven:3.9.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/moneymanager-0.0.1-SNAPSHOT.jar moneymanager-v1.0.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","moneymanager-v1.0.jar"]
