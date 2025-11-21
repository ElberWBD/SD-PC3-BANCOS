# Etapa de build: compila la aplicación con Maven
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -DskipTests package

# Etapa de runtime: solo Java para ejecutar el JAR
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV DATA_DIR=/app/data
RUN mkdir -p /app/data
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
