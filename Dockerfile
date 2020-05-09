FROM openjdk:8-jre-alpine3.9

RUN mkdir /app

WORKDIR /app

COPY target/blog-previewer-0.0.1.jar /app

EXPOSE 8080

CMD [ "java", "-jar", "blog-previewer-0.0.1.jar" ]