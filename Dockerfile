FROM eclipse-temurin:17

WORKDIR /app

# Copy the entire project (including target directory)
COPY . .

# The JAR should already be built locally or we need to ensure it exists
RUN ls -la target/ || echo "JAR not found - make sure to build locally first"

# Copy the pre-built JAR (if it exists)
COPY target/backend-1.0.jar app.jar || echo "Using fallback - copying source files"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=production"]
