FROM anapsix/alpine-java
VOLUME /tmp
ENV JAVA_OPTS="-Xmx256m -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]

ADD "dms-2.jar" "/app.jar
EXPOSE 2223 2223