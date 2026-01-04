# Étape 1 : Build avec Maven et Java 21
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Run avec JRE 21
FROM eclipse-temurin:21-jre
WORKDIR /app
# On récupère le jar généré
COPY --from=build /app/target/*.jar app.jar

# Railway utilise la variable PORT, on ne doit pas forcer 8080
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-Xmx512m", "-Dserver.port=${PORT}", "-jar", "app.jar"]
