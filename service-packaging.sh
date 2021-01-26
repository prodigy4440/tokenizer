#!/bin/bash

pushd data-source/
mvn package -pl .
popd

pushd data-flow/
mvn package -pl .
popd

pushd data-proof/
mvn package -pl .
popd