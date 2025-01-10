FROM openjdk:22 AS build
WORKDIR /app
COPY . .
RUN chmod +x ./mvnw
RUN ./mvnw clean install -DskipTests

FROM openjdk:22
WORKDIR /app
COPY --from=build /app/target/*.jar backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]