FROM amazoncorretto:23.0.1-alpine

WORKDIR /app

COPY target/recipes-0.0.1-SNAPSHOT.jar /app/recipes-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./recipes-0.0.1-SNAPSHOT.jar"]