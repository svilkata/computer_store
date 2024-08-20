FROM amazoncorretto:17.0.6
COPY target/computer_store-2.6.7.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]