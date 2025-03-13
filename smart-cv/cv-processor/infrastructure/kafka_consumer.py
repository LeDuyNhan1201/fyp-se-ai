import os
from abc import abstractmethod

from confluent_kafka import Consumer
from confluent_kafka.schema_registry.protobuf import ProtobufDeserializer
from confluent_kafka.serialization import SerializationContext, MessageField

class KafkaConsumer:
    def __init__(self, schema, topic: str, group_id: str):
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
        self.instance.subscribe(self.topics)
        while True:
            try:
                message = self.instance.poll(1.0)
                if message is None: continue
                command = self.deserializer(message.value, SerializationContext(self.topics[0], MessageField.VALUE))
                print(f"Received command: {command}")

                self.handle(command)

            except KeyboardInterrupt:
                break

        self.instance.close()

