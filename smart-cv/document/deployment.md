# DOCKER
````shell
docker-compose -f deployment/dev/docker-compose.dev.yml -f deployment/dev/message-queue.dev.yml config

docker-compose -f deployment/dev/docker-compose.dev.yml -f deployment/dev/message-queue.dev.yml \
-f deployment/dev/databases.dev.yml -f deployment/dev/file-service.dev.yml -f deployment/dev/ui-console.dev.yml up -d 
````