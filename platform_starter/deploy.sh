#!/bin/sh

docker build -f ./Dockerfile -t js/management_platform:$1 ./
docker run --name js -d -p 8080:8080 -v /js/logs:/js/logs js/management_platform:$1

echo "deployed"
