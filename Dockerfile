FROM openjdk:17-jdk-alpine
COPY build/libs/test-task-banking-core-1.0.0.jar .
ENTRYPOINT ["java","-jar","/test-task-banking-core-1.0.0.jar"]
