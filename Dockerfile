FROM maven:onbuild-alpine

EXPOSE 8080

CMD ["java", "-Xmx400m", "-jar", "/usr/src/app/target/pdf-1.0-SNAPSHOT.jar"]
