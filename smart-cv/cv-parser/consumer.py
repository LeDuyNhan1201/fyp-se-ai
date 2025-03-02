import protobuf.cv_command_pb2 as command_pb2
from confluent_kafka import Consumer
from confluent_kafka.serialization import SerializationContext, MessageField
from confluent_kafka.schema_registry.protobuf import ProtobufDeserializer

def main():
    topic = "cv.commands"
    bootstrap_servers = "localhost:9092"
    schema_registry = "http://localhost:8085"
    group_id = "cv-parser"

    protobuf_deserializer = ProtobufDeserializer(command_pb2.,
                                                 {'use.deprecated.format': False})

    consumer_conf = {'bootstrap.servers': bootstrap_servers,
                     'group.id': group_id,
                     'auto.offset.reset': "earliest"}

    consumer = Consumer(consumer_conf)
    consumer.subscribe([topic])

    while True:
        try:
            # SIGINT can't be handled when polling, limit timeout to 1 second.
            msg = consumer.poll(1.0)
            if msg is None:
                continue

            user = protobuf_deserializer(msg.value(), SerializationContext(topic, MessageField.VALUE))

            if user is not None:
                print("User record {}:\n"
                      "\tname: {}\n"
                      "\tfavorite_number: {}\n"
                      "\tfavorite_color: {}\n"
                      .format(msg.key(), user.name,
                              user.favorite_number,
                              user.favorite_color))
        except KeyboardInterrupt:
            break

    consumer.close()

if __name__ == '__main__':
    main()
