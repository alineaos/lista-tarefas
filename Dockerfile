FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml /app

RUN mvn dependency:go-offline -B

COPY src /app/src

RUN mvn clean install


FROM eclipse-temurin:21-jre-alpine

COPY --from=build /app/target/lista-tarefas-1.0-SNAPSHOT.jar /app/app.jar

WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]