FROM openjdk:17
ARG JAR_FILE=demo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} shoplive.jar
ENTRYPOINT ["java","-jar","/shoplive.jar"]
