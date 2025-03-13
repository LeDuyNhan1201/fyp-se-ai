import os

from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.protobuf import ProtobufSerializer, ProtobufDeserializer
from confluent_kafka.serialization import StringSerializer


class KafkaConfig:
    def __init__(self):
        self.bootstrap_servers = os.getenv("BOOTSTRAP_SERVERS")
        self.schema_registry_url = os.getenv("SCHEMA_REGISTRY_URL")

    def consumer_config(self, schema, topics: list, group_id: str) -> dict:
        return {
            'props': {
                'topics': topics,
                'bootstrap.servers': self.bootstrap_servers,
                'group.id': group_id,
                'auto.offset.reset': "earliest"
            },
            'deserializer': ProtobufDeserializer(schema, {'use.deprecated.format': False})
        }

    def producer_template(self, schema) -> dict:
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
