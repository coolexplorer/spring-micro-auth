FROM adoptopenjdk:11-jre-hotspot

ARG WAR_FILE=./target/*.war
ARG PROFILE

ENV SPRING_PROFILE=$PROFILE
COPY ${WAR_FILE} webapp.war

CMD ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "webapp.war"]