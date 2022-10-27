FROM openjdk:11
EXPOSE 8082
ADD target/notes-be-0.0.1-SNAPSHOT.jar 
ENTRYPOINT ["java", "-jar", "notes-be-0.0.1-SNAPSHOT.jar"]
MAINTAINER saxenamanish
ENV myenv2 myval2




# We can easily change the base image in order to use a #different Java version. For example, if we want to use the  #Corretto distribution from Amazon, we can simply change the
# just change fist line
# FROM amazoncorretto:11-alpine-jdk
