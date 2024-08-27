FROM openjdk:17
COPY yeojeong-0.0.1-SNAPSHOT.jar docker-springboot.jar
ENTRYPOINT ["java", "-jar", "/docker-springboot.jar"]