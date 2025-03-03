# DOCKER
docker-compose -f deployment/docker-compose.yml -f deployment/message-queue.yml config
docker-compose -f deployment/docker-compose.yml -f deployment/message-queue.yml \
-f deployment/file-service.yml -f deployment/databases.yml up -d

