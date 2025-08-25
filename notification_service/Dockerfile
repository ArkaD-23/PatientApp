FROM eclipse-temurin:17-jre

ARG JAR_FILE=target/notification_service-0.0.1-SNAPSHOT.jar

WORKDIR /app

COPY ${JAR_FILE} app.jar

EXPOSE 8089

ENTRYPOINT ["java","-jar","/app/app.jar"]
