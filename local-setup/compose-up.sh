#!/bin/bash
set -uex

scriptDir="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
projectDir=$(dirname "${scriptDir}")
image=fake-registry.thinkandcode.pl/samples/todo-app-sample:latest

cd "${projectDir}"
java --version
./gradlew -s build -x check
docker build -f "${projectDir}/docker/Dockerfile" -t "${image}" "${projectDir}/build/libs"

cd "${scriptDir}"

# this permission is an overkill, but for local setup it's not to bad
chmod 777 "${scriptDir}/grafana/data/grafana.db"

docker-compose down
docker-compose up -d --force-recreate

# sleep 10s
sleep 10