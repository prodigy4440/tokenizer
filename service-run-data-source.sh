#!/bin/bash

export KAFKA_URL=localhost:9092
export PRODUCER_TOPIC=card-info-topic
java  -jar data-source/target/data-source-1.0-SNAPSHOT.jar server data-source/config.yml