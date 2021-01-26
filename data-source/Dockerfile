FROM java:8-jre
COPY config.yml /opt/source/
COPY target/data-source-1.0-SNAPSHOT.jar /opt/proof/
EXPOSE 8080
WORKDIR /opt/source
CMD ["java","-jar", "data-source-1.0-SNAPSHOT.jar", "server", "config.yml"]