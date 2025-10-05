# Use Maven image to build
FROM maven:3.9.3-eclipse-temurin-20 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Use JDK image for runtime
FROM eclipse-temurin:20-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/moneymanager-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
