FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml /app

RUN mvn dependency:go-offline -B

COPY src /app/src

RUN mvn clean install


FROM eclipse-temurin:21-jre-alpine

COPY --from=build /app/target/lista-tarefas-1.0-SNAPSHOT-jar-with-dependencies.jar /app/app.jar
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["/bin/sh", "/app/entrypoint.sh"]