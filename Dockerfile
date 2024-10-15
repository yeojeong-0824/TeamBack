FROM openjdk:17
COPY build/libs/*.jar /deploy/app.jar
EXPOSE 8080
CMD java -jar -Dserver.port=8080 /deploy/app.jar