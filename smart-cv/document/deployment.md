# DOCKER
````shell
docker-compose -f deployment/dev/docker-compose.dev.yml -f deployment/dev/message-queue.dev.yml config

sudo chmod 777 -R deployment/dev/

docker-compose -f deployment/dev/docker-compose.dev.yml -f deployment/dev/databases.dev.yml \
-f deployment/dev/message-queue.dev.yml -f deployment/dev/file-service.dev.yml \
-f deployment/dev/metrics-monitoring.dev.yml -f deployment/dev/ui-console.dev.yml up -d
````