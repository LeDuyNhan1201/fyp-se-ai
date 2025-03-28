# DOCKER
````shell
docker-compose -f deployment/docker-compose.yml -f deployment/message-queue.dev.yml config
docker-compose -f deployment/docker-compose.yml -f deployment/message-queue.dev.yml \
-f deployment/databases.dev.yml -f deployment/file-service.dev.yml -f deployment/ui-console.dev.yml up -d
````