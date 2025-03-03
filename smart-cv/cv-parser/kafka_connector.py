import os
import protobuf.cv.command_pb2 as cv_command_pb2
from google.protobuf.symbol_database import Default
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.protobuf import ProtobufSerializer, ProtobufDeserializer
from confluent_kafka.serialization import StringSerializer
from dotenv import load_dotenv

load_dotenv()
BOOTSTRAP_SERVERS = os.getenv("BOOTSTRAP_SERVERS")
SCHEMA_REGISTRY_URL = os.getenv("SCHEMA_REGISTRY_URL")
KAFKA_TOPIC_CV_COMMAND = os.getenv("KAFKA_TOPIC_CV_COMMAND")
KAFKA_GROUP_CV_PROCESSOR = os.getenv("KAFKA_GROUP_CV_PROCESSOR")

# Đăng ký file descriptor
sym_db = Default()
sym_db.RegisterFileDescriptor(cv_command_pb2.DESCRIPTOR)

# Lấy message từ protobuf symbol database
parse_cv_command = sym_db.GetSymbol("com.ben.smartcv.common.ParseCvCommand")

def consumer_config() -> dict:
    return {
        'props': {
            'topics': [ KAFKA_TOPIC_CV_COMMAND ],
            'bootstrap.servers': BOOTSTRAP_SERVERS,
            'group.id': KAFKA_GROUP_CV_PROCESSOR,
            'auto.offset.reset': "earliest"
        },
        'deserializer': ProtobufDeserializer(parse_cv_command, {'use.deprecated.format': False})
    }

def producer_config() -> dict:
    # Kết nối Schema Registry
    schema_registry_client = SchemaRegistryClient({'url': SCHEMA_REGISTRY_URL})

    # Serializer cho key và value
    string_serializer = StringSerializer('utf8')
    protobuf_serializer = ProtobufSerializer(parse_cv_command,
                                             schema_registry_client, {'use.deprecated.format': False})
    return {
        'props': {
            'bootstrap.servers': BOOTSTRAP_SERVERS
        },
        'serializer': protobuf_serializer,
        'key_serializer': string_serializer
    }
