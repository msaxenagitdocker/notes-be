FROM openjdk:11
EXPOSE 8082
ADD target/notes-be-0.0.1-SNAPSHOT.jar 
ENTRYPOINT ["java", "-jar", "notes-be-0.0.1-SNAPSHOT.jar"]
MAINTAINER saxenamanish
ENV myenv2 myval2