import logging
import os
from abc import abstractmethod

from confluent_kafka import Consumer, KafkaException
from confluent_kafka.schema_registry.protobuf import ProtobufDeserializer
from confluent_kafka.serialization import SerializationContext, MessageField
from dotenv import load_dotenv

from infrastructure.kafka_consumer import KafkaConsumer
from application.constant import KAFKA_TOPIC_JOB_COMMAND, KAFKA_TOPIC_NOTIFICATION_COMMAND
from application.proto_message import SEND_NOTIFICATION_COMMAND, \
    ROLLBACK_PROCESS_JOB_COMMAND
from cv_parser import extract_job_info
from infrastructure.elasticsearch_client import ElasticsearchClient
from infrastructure.kafka_producer import KafkaProducer

logger = logging.getLogger(__name__)
logging.basicConfig(
    level = logging.INFO,
    format = f"%(asctime)s - {__name__} - %(levelname)s - %(message)s"
)

class ProcessJobConsumer:
    def __init__(self, schema, topic: str, group_id: str,
                 elasticsearch_client: ElasticsearchClient,
                 rollback_producer: KafkaProducer,
                 send_noti_producer: KafkaProducer):
        load_dotenv()
        bootstrap_servers = os.getenv("BOOTSTRAP_SERVERS")
        self.config = {
            'bootstrap.servers': bootstrap_servers,
            'group.id': group_id,
            'auto.offset.reset': "earliest",
            'enable.auto.commit': False
        }
        self.instance = Consumer(self.config)
        self.topics = [topic]
        self.deserializer = ProtobufDeserializer(schema, {'use.deprecated.format': False})
        logger.info(f"Deserializer type: {type(self.deserializer)}")
        self.instance.subscribe(self.topics)

        self.elasticsearch_client = elasticsearch_client
        self.rollback_producer = rollback_producer
        self.send_noti_producer = send_noti_producer

    def handle(self, message):
        print(message)
        job = self.elasticsearch_client.get_document("jobs", message.job_id)
        data = extract_job_info(job["raw_text"])
        logger.info(data)
        self.elasticsearch_client.update_document("jobs", message.job_id, data)

        if not validation(data):
            fail_command = ROLLBACK_PROCESS_JOB_COMMAND(
                job_id = message.job_id
            )
            self.rollback_producer.publish(KAFKA_TOPIC_JOB_COMMAND, fail_command)

        success_command = SEND_NOTIFICATION_COMMAND(
            association_property = message.job_id,
            title = "Job Processed",
            content = "Job processed successfully"
        )
        self.send_noti_producer.publish(KAFKA_TOPIC_NOTIFICATION_COMMAND, success_command)
        # Save to database

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
                        logger.info(f"Kafka commit failed: {e}")

            except KafkaException as e:
                logger.error(f"Kafka error: {e}")
                break  # Thoát vòng lặp nếu có lỗi Kafka nghiêm trọng

            except KeyboardInterrupt:
                logger.info("Consumer interrupted by user")
                break

        # Đóng consumer khi thoát
        self.instance.close()
        logger.info("Consumer closed")

def validation(data) -> bool:
    return True





