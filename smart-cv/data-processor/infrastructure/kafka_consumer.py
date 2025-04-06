import logging
import os
from abc import abstractmethod

from confluent_kafka import Consumer, KafkaException
from confluent_kafka.schema_registry.protobuf import ProtobufDeserializer
from confluent_kafka.serialization import SerializationContext, MessageField
from dotenv import load_dotenv

logger = logging.getLogger(__name__)
logging.basicConfig(
    level = logging.INFO,
    format = f"%(asctime)s - {__name__} - %(levelname)s - %(message)s"
)

class KafkaConsumer:
    def __init__(self, schema, topic: str, group_id: str):
        load_dotenv()
        bootstrap_servers = os.getenv("BOOTSTRAP_SERVERS")
        self.config = {
            'bootstrap.servers': bootstrap_servers,
            'group.id': group_id,
            'auto.offset.reset': "earliest"
        }
        self.instance = Consumer(self.config)
        self.topics = [ topic ]
        self.deserializer = ProtobufDeserializer(schema, {'use.deprecated.format': False})

    @abstractmethod
    def handle(self, message):
        pass

    def consume(self):
        messages_processed = 0  # Đếm số message đã xử lý
        batch_size = 5  # Commit sau mỗi 5 messages

        while True:
            try:
                message = self.instance.poll(1.0)  # Tăng timeout lên 5 giây
                if message is None:
                    logger.info("Waiting for new messages...")  # Log kiểm tra vòng lặp
                    continue

                if message.error():
                    logger.info(f"Consumer error: {message.error()}")
                    continue

                # Deserialize message
                command = self.deserializer(message.value(), SerializationContext(self.topics[0], MessageField.VALUE))
                logger.info(f"Received command: {command}")

                # Xử lý message
                self.handle(command)

                # Tăng đếm số message đã xử lý
                messages_processed += 1

                # Commit sau mỗi batch_size messages
                if messages_processed % batch_size == 0:
                    try:
                        self.instance.commit()
                        logger.info("Committed batch of messages")
                    except KafkaException as e:
                        logger.error(f"Kafka commit failed: {e}")

            except KafkaException as e:
                logger.error(f"Kafka error: {e}")
                break  # Thoát vòng lặp nếu có lỗi Kafka nghiêm trọng

            except KeyboardInterrupt:
                logger.info("Consumer interrupted by securityUser")
                break

        # Đóng consumer khi thoát
        self.instance.close()
        logger.info("Consumer closed")

