FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /workspace/app

# Copy maven executable and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x ./mvnw

# Download all required dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Disable Liquibase in application.yml for production to prevent issues with existing tables
RUN sed -i 's/enabled: true/enabled: false/g' src/main/resources/application.yml

# Build the application
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Production image
FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency

# Copy the dependency application file structure
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Create uploads directory for local storage option
RUN mkdir -p /app/uploads && chmod 777 /app/uploads

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=prod
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/twitterclone
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres
ENV SPRING_REDIS_HOST=redis
ENV SPRING_RABBITMQ_HOST=rabbitmq
ENV SERVER_PORT=8081
ENV SPRING_LIQUIBASE_ENABLED=false

# Run as non-root user for better security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.dhillon.twitterclone.TwitterCloneApplication"] 