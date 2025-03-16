import logging
from abc import abstractmethod

from infrastructure.kafka_consumer import KafkaConsumer
from application.utils import get_file_extensions
from cv_parser import extract_cv_info, match_cv_with_jd
from extract_data_from_file import extract_text_from_pdf, extract_text_from_image
from infrastructure.kafka_producer import KafkaProducer
from infrastructure.minio_client import MinioClient

logger = logging.getLogger(__name__)
logging.basicConfig(
    level = logging.INFO,
    format = f"%(asctime)s - {__name__} - %(levelname)s - %(message)s"
)

class ProcessCvConsumer(KafkaConsumer):
    def __init__(self, schema, topic: str, group_id: str,
                 file_storage_client: MinioClient,
                 rollback_producer: KafkaProducer,
                 send_noti_producer: KafkaProducer):
        super().__init__(schema, topic, group_id)
        self.file_storage_client = file_storage_client
        self.rollback_producer = rollback_producer
        self.send_noti_producer = send_noti_producer

    @abstractmethod
    def handle(self, message):
        file_path = self.file_storage_client.download_file(message.object_key)
        raw_text = extract_text_from_pdf(file_path) \
            if get_file_extensions(message.object_key) == "pdf" else extract_text_from_image(file_path)
        data = extract_cv_info(raw_text)
        logger.info(data)
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
        logger.info(score)

        # if not validation(data):
        #     fail_command = ROLLBACK_PROCESS_CV_COMMAND(
        #         cvId = message.cv_id,
        #         objectKey = message.object_key
        #     )
        #     self.rollback_producer.publish(KAFKA_TOPIC_JOB_COMMAND, fail_command)
        #
        # success_command = SEND_NOTIFICATION_COMMAND(
        #     association_property = message.cv_id,
        #     title = "CV Processed",
        #     content = "CV processed successfully"
        # )
        # self.send_noti_producer.publish(KAFKA_TOPIC_NOTIFICATION_COMMAND, success_command)
        # Save to database

def validation(data) -> bool:
    return True





