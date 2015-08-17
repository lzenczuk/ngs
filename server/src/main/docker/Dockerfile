FROM java:8
ADD ngs-server-0.1.0.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8088
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
