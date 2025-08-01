# ---- Build Stage ----
FROM maven:3.9.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ---- Run Stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/gdp-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENV PORT 8080

ENTRYPOINT ["java", "-jar", "app.jar"]