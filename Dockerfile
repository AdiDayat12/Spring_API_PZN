FROM openjdk:21-slim

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar


RUN mkdir -p /usr/local/newrelic
ADD ./newrelic/newrelic.jar /usr/local/newrelic/newrelic.jar
ADD ./newrelic/newrelic.yml /usr/local/newrelic/newrelic.yml


ARG NEW_RELIC_LICENSE_KEY
ENV NEW_RELIC_LICENSE_KEY=${NEW_RELIC_LICENSE_KEY}
ENV NEW_RELIC_APP_NAME=spring_api_net


ENTRYPOINT ["java",
  "-javaagent:/usr/local/newrelic/newrelic.jar",
  "-Dspring.config.additional-location=classpath:/,file:./",
  "-Dspring.profiles.active=default",
  "-jar", "/app.jar"
]
