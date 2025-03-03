from uuid import uuid4
from confluent_kafka import Producer
from confluent_kafka.serialization import SerializationContext, MessageField

def delivery_report(err, msg):
    if err is not None:
        print(f"Delivery failed for User record {msg.key()}: {err}")
    else:
        print(f'User record {msg.key()} successfully produced to {msg.topic()} [{msg.partition()}] at offset {msg.offset()}')

def produce_message(config: dict, topic, msg):
    producer = Producer(config)
    producer.produce(
        topic = topic,
        partition = 0,
        key = config['key_serializer'](str(uuid4())),
        value = config['serializer'](msg, SerializationContext(topic, MessageField.VALUE)),
        on_delivery = delivery_report
    )
    producer.flush()