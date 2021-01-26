FROM java:8-jre
COPY config.yml /opt/proof/
COPY target/data-proof-1.0-SNAPSHOT.jar /opt/proof/
EXPOSE 8080
WORKDIR /opt/proof
CMD ["java","-jar", "data-proof-1.0-SNAPSHOT.jar", "server", "config.yml"]