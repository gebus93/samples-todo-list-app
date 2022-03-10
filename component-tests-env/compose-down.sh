#!/bin/bash
set -uex

scriptDir="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
cd "${scriptDir}"
docker-compose down