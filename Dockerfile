FROM eclipse-temurin:21-jre
WORKDIR/app
COPY target/moneymanager-0.0.1-SNAPSHOT.jar moneymanager.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "moneymanager.jar"]