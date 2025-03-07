from confluent_kafka.cimpl import Consumer
from confluent_kafka.serialization import SerializationContext, MessageField

from KafkaConfig import KafkaConfig
from cv_parser import extract_cv_info, match_cv_with_jd
from extract_data_from_file import extract_text_from_pdf, extract_text_from_image
from minio_client import download_file
from producer import produce_message
from protobuf.notification import command_pb2 as notification_pb2
from protobuf.cv import command_pb2 as cv_command_pb2


def get_file_extensions(filenames):
    return [filename.split('.')[-1] for filename in filenames]

def validation(data) -> bool:
    return True

kafka_conf = KafkaConfig()

def process_cv(command):
    file_path = download_file(command["objectKey"])
    raw_text = extract_text_from_pdf(file_path) \
        if get_file_extensions(command["objectKey"]) == "pdf" else extract_text_from_image(file_path)
    data = extract_cv_info(raw_text)
    print(data)
    job_description = """
                Job Type: Full-time
                Experience Level: Mid-Senior

                Job Description:
                We are seeking an IoT Embedded Systems Engineer to design, develop, and optimize embedded systems for IoT applications. The ideal candidate will have experience in embedded programming, sensor integration, and wireless communication protocols.

                Responsibilities:
                Develop firmware for IoT devices using C/C++ or Python.
                Work with microcontrollers (ESP32, STM32, ARM Cortex).
                Integrate sensors and peripherals via SPI, I2C, UART, and GPIO.
                Optimize power consumption and system performance.
                Ensure IoT security and data encryption.
                Collaborate with cloud developers to integrate IoT devices with platforms like AWS IoT, Azure IoT, or Google Cloud IoT.
                Requirements:
                Bachelor's/Master’s degree in Computer Science, Electrical Engineering, or related field.
                Strong experience with embedded C/C++ and RTOS.
                Familiarity with wireless communication protocols (BLE, LoRa, Zigbee, MQTT).
                Hands-on experience with IoT platforms and device-to-cloud communication.
                Knowledge of PCB design and hardware debugging is a plus.
                """
    score = match_cv_with_jd(data, job_description)
    print(score)

    if not validation(data):
        producer_config = kafka_conf.producer_config(kafka_conf.process_cv_command)
        produce_message(producer_config, kafka_conf.kafka_topic_cv_command, command)
        fail_command = kafka_conf.rollback_process_cv_command
        fail_command.cvId = command["cvId"]
        fail_command.objectKey = command["objectKey"]

    producer_config = kafka_conf.producer_config(kafka_conf.send_notification_command)
    produce_message(producer_config, kafka_conf.kafka_topic_cv_command, command)
    success_command = kafka_conf.send_notification_command
    success_command.associationProperty = command["cvId"]
    success_command.title = "CV Processed"
    success_command.content = "CV processed successfully"
    # Save to database


def init_consumer():
    config = kafka_conf.consumer_config(kafka_conf.process_cv_command)
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

            process_cv(command)

        except KeyboardInterrupt:
            break

    consumer.close()

