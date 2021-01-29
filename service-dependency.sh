#!/bin/bash

kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic data-input

#same as above
kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic data-output