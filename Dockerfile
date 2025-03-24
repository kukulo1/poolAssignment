FROM eclipse-temurin:17-jdk-alpine
COPY build/libs/test_assignment.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]