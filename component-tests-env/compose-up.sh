#!/bin/bash
set -uex

scriptDir="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
projectDir=$(dirname "${scriptDir}")
image=fake-registry.thinkandcode.pl/samples/todo-app-sample:latest

cd "${projectDir}"
./gradlew -s build -x check
docker build -f "${projectDir}/docker/Dockerfile" -t "${image}" "${projectDir}/build/libs"

cd "${scriptDir}"
docker-compose down
docker-compose up -d --force-recreate