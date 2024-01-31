FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} tzhat.jar
ENTRYPOINT ["java","-jar","/tzhat.jar"]