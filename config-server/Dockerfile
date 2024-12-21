FROM amazoncorretto:23.0.1-alpine

WORKDIR /app

COPY target/config-server-0.0.1-SNAPSHOT.jar /app/config-server-0.0.1-SNAPSHOT.jar

EXPOSE 8071

ENTRYPOINT ["java", "-jar", "./config-server-0.0.1-SNAPSHOT.jar"]