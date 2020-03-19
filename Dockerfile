## Add maintainer info
MAINTAINER Marcelo Ferreira Vasconcelos <marcelo.fvas@gmail.com>

## Starts with a base image containing Java runtime environment
FROM openjdk:11-jre-slim

## Add a volume pointing to /tmp
VOLUME /tmp

## Make port 80 available outside the container
EXPOSE 80:80

## Set the application's jar file location
ARG JAR_FILE=target/websocket-secured-chat-1.0.0-SNAPSHOT.jar

## Set the Java Options configuration
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS

## Add/copy the application's jar file to the container
ADD ${JAR_FILE} app.jar

## Run the jar file 
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar

##
## For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar
#ENTRYPOINT exec java -jar -Dspring.profiles.active=dev app.jar
