# DOCKER
docker-compose -f deployment/docker-compose.yml -f deployment/message-queue.yml config
docker-compose -f deployment/docker-compose.yml -f deployment/message-queue.yml up -d

