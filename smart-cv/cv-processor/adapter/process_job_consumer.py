import os
from abc import abstractmethod

from confluent_kafka import Consumer
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
        print(f"Deserializer type: {type(self.deserializer)}")
        self.instance.subscribe(self.topics)

        self.elasticsearch_client = elasticsearch_client
        self.rollback_producer = rollback_producer
        self.send_noti_producer = send_noti_producer

    def handle(self, message):
        print(message)
        job = self.elasticsearch_client.get_document("jobs", message.job_id)
        data = extract_job_info(job["raw_text"])
        print(data)
        self.elasticsearch_client.update_document("jobs", message.job_id, data)

        if not validation(data):
            fail_command = ROLLBACK_PROCESS_JOB_COMMAND
            fail_command.cvId = message.job_id
            self.rollback_producer.publish(KAFKA_TOPIC_JOB_COMMAND, fail_command)

        success_command = SEND_NOTIFICATION_COMMAND(
            association_property = message.job_id,
            title = "Job Processed",
            content = "Job processed successfully"
        )
        self.send_noti_producer.publish(KAFKA_TOPIC_NOTIFICATION_COMMAND, success_command)
        # Save to database

    def consume(self):
        while True:
            try:
                message = self.instance.poll(1.0)
                if message is None: continue
                print(f"Message value type: {type(message.value)}, value: {message.value}")
                command = self.deserializer(message.value(), SerializationContext(self.topics[0], MessageField.VALUE))
                print(f"Received command: {command}")

                self.handle(command)

                self.instance.commit(message)

            except KeyboardInterrupt:
                break

        self.instance.close()

def validation(data) -> bool:
    return True





