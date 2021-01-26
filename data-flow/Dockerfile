FROM java:8-jre
COPY config.yml /opt/flow/
COPY target/data-flow-1.0-SNAPSHOT.jar /opt/flow/
EXPOSE 8080
WORKDIR /opt/flow
CMD ["java","-jar", "data-flow-1.0-SNAPSHOT.jar", "server", "config.yml"]