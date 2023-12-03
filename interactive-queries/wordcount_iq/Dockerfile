ARG BUILD_HOME=/app

# Builder ############################################
FROM maven:3.8.5-openjdk-17-slim AS builder

RUN  apt-get update \
  && apt-get install -y wget \
  && rm -rf /var/lib/apt/lists/*

ENV APP_HOME=$BUILD_HOME
WORKDIR $APP_HOME  

COPY src $APP_HOME/src
COPY pom.xml $APP_HOME
RUN mvn -f $APP_HOME/pom.xml clean package  

# App ############################################
FROM openjdk:17-slim

COPY --from=builder $APP_HOME/target/wordcount-iq-jar-with-dependencies.jar app.jar

EXPOSE 38080

ENV APP_NAME="java-producer"  

CMD ["java", "-jar", "app.jar","/config/config.properties"]