FROM marhan/rpi-java8
VOLUME /tmp
ADD "target/registration-0.0.1-SNAPSHOT.jar" "/app.jar"
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n"

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]

EXPOSE 18080 18080