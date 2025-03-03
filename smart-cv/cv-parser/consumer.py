from confluent_kafka import Consumer
from confluent_kafka.serialization import SerializationContext, MessageField

def init_consumer(config: dict):
    protobuf_deserializer = config['deserializer']
    consumer = Consumer(config)
    consumer.subscribe(config['props']['topics'])
    topic = config['props']['topics'][0]

    while True:
        try:
            msg = consumer.poll(1.0)
            if msg is None: continue
            command = protobuf_deserializer(msg.value, SerializationContext(topic, MessageField.VALUE))
            print(f"Received command: {command}")

        except KeyboardInterrupt:
            break

    consumer.close()

