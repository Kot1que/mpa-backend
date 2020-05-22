FROM openjdk:11

VOLUME /tmp

ADD build/libs/contraindications-0.0.1-SNAPSHOT.jar server.jar
ENTRYPOINT ["java", "-jar", "server.jar"]
