FROM amazoncorretto:21-alpine
WORKDIR /app

COPY .env /app/.env

COPY target/jellycash-0.0.1-SNAPSHOT.jar /app/

EXPOSE 8080

CMD ["java", "-jar", "jellycash-0.0.1-SNAPSHOT.jar"]