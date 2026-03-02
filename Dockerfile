FROM eclipse-temurin:17

WORKDIR /app

# Copy Maven wrapper and pom.xml first
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Build the application
RUN chmod +x ./mvnw && ./mvnw clean package -DskipTests

# Copy the built JAR
RUN cp target/backend-1.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=production"]
