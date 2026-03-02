FROM eclipse-temurin:17

WORKDIR /app

# Copy entire project (including target directory)
COPY . .

# Copy the pre-built JAR
COPY target/backend-1.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=production"]
