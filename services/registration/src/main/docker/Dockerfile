FROM anapsix/alpine-java
VOLUME /tmp
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
ENV JAVA_OPTS="-Xmx256m -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"

ADD "registration-0.0.1-SNAPSHOT.jar" "/app.jar"
EXPOSE 18081 18081