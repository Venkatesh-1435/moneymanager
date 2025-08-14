FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/MoneyManager-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 9001

#MoneyManager-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
