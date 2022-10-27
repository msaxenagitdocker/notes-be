FROM openjdk:11
EXPOSE 8082
ADD target/notes-be-0.0.1-SNAPSHOT.jar 
ENTRYPOINT ["java", "-jar", "notes-be-0.0.1-SNAPSHOT.jar"]
MAINTAINER saxenamanish
ENV myenv2 myval2




# We can easily change the base image in order to use a #different Java version. For example, if we want to use the  #Corretto distribution from Amazon, we can simply change the
# just change fist line
# FROM amazoncorretto:11-alpine-jdk



# version: Specifies which format version should be used. This is a mandatory field. Here we use the newer version, whereas the legacy format is ‘1'.
# services: Each object in this key defines a service, a.k.a container. This section is mandatory.
# build: If given, docker-compose is able to build an image from a Dockerfile
# context: If given, it specifies the build-directory, where the Dockerfile is looked-up.
# dockerfile: If given, it sets an alternate name for a Dockerfile.
# image: Tells Docker which name it should give to the image when build-features are used. Otherwise, it's searching for this image in the library or remote-registry.
# networks: This is the identifier of the named networks to use. A given name-value must be listed in the networks section.
# networks: In this section, we're specifying the networks available to our services. In this example, we let docker-compose create a named network of type ‘bridge' for us. If the option external is set to true, it will use an existing one with the given name.
# Before we continue, we'll check our build-file for syntax-errors:
# $> docker-compose config


