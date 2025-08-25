FROM eclipse-temurin:17-jre

ARG JAR_FILE=target/proto-0.0.1-SNAPSHOT.jar

WORKDIR /app

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
