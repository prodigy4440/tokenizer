#!/bin/bash

export REDIS_URL=localhost:6379
export KAFKA_URL=localhost:9092
export PRODUCER_TOPIC=input-data
export CONSUMER_TOPIC=outout-data

java  -jar data-proof/target/data-proof-1.0-SNAPSHOT.jar server data-proof/config.yml