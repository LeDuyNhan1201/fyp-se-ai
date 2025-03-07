import os
import protobuf.cv.command_pb2 as cv_command_pb2
from google.protobuf.symbol_database import Default
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.protobuf import ProtobufSerializer, ProtobufDeserializer
from confluent_kafka.serialization import StringSerializer
from dotenv import load_dotenv

class KafkaConfig:
    def __init__(self):
        load_dotenv()
        self.bootstrap_servers = os.getenv("BOOTSTRAP_SERVERS")
        self.schema_registry_url = os.getenv("SCHEMA_REGISTRY_URL")
        self.kafka_topic_cv_command = os.getenv("KAFKA_TOPIC_CV_COMMAND")
        self.kafka_group_cv_processor = os.getenv("KAFKA_GROUP_CV_PROCESSOR")

        # Đăng ký file descriptor
        self.sym_db = Default()
        self.sym_db.RegisterFileDescriptor(cv_command_pb2.DESCRIPTOR)

        # Lấy message từ protobuf symbol database
        self.process_cv_command = self.sym_db.GetSymbol("com.ben.smartcv.common.ProcessCvCommand")
        self.rollback_process_cv_command = self.sym_db.GetSymbol("com.ben.smartcv.common.RollbackProcessCvCommand")
        self.send_notification_command = self.sym_db.GetSymbol("com.ben.smartcv.common.SendNotificationCommand")

    def consumer_config(self, schema) -> dict:
        return {
            'props': {
                'topics': [self.kafka_topic_cv_command],
                'bootstrap.servers': self.bootstrap_servers,
                'group.id': self.kafka_group_cv_processor,
                'auto.offset.reset': "earliest"
            },
            'deserializer': ProtobufDeserializer(schema, {'use.deprecated.format': False})
        }

    def producer_config(self, schema) -> dict:
        # Kết nối Schema Registry
        schema_registry_client = SchemaRegistryClient({'url': self.schema_registry_url})

        # Serializer cho key và value
        string_serializer = StringSerializer('utf8')
        protobuf_serializer = ProtobufSerializer(schema, schema_registry_client, {'use.deprecated.format': False})

        return {
            'props': {
                'bootstrap.servers': self.bootstrap_servers
            },
            'serializer': protobuf_serializer,
            'key_serializer': string_serializer
        }
