import os
from uuid import uuid4
from confluent_kafka import Producer
from confluent_kafka.schema_registry import SchemaRegistryClient
from confluent_kafka.schema_registry.protobuf import ProtobufSerializer
from confluent_kafka.serialization import SerializationContext, MessageField, StringSerializer

def delivery_report(err, msg):
    if err is not None:
        print(f"Delivery failed for User record {msg.key()}: {err}")
    else:
        print(f'User record {msg.key()} successfully produced to {msg.topic()} [{msg.partition()}] at offset {msg.offset()}')

class KafkaProducer:
    def __init__(self, schema):
        schema_registry_url = os.getenv("SCHEMA_REGISTRY_URL")
        bootstrap_servers = os.getenv("BOOTSTRAP_SERVERS")
        # Kết nối Schema Registry
        schema_registry_client = SchemaRegistryClient({'url': schema_registry_url})
        # Serializer cho key và value
        string_serializer = StringSerializer('utf8')
        protobuf_serializer = ProtobufSerializer(schema, schema_registry_client, {'use.deprecated.format': False})

        self.template = {
            'bootstrap.servers': bootstrap_servers,
        }
        self.instance = Producer(self.template)
        self.key_serializer = string_serializer
        self.value_serializer = protobuf_serializer

    def publish(self, topic, msg):
        self.instance.produce(
            topic = topic,
            partition = 0,
            key = self.key_serializer(str(uuid4())),
            value = self.value_serializer(msg, SerializationContext(topic, MessageField.VALUE)),
            on_delivery = delivery_report
        )
        self.instance.flush()