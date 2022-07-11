#!/bin/sh

docker build -f ./Dockerfile -t js/portal:$1 ./
docker run --name portal -d -p 80:80 js/portal:$1

echo "deployed"
