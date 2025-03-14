from adapter.process_cv_consumer import ProcessCvConsumer
from adapter.process_job_consumer import ProcessJobConsumer
from application.constant import KAFKA_TOPIC_JOB_COMMAND, KAFKA_GROUP_CV_PROCESSOR, KAFKA_TOPIC_CV_COMMAND, \
    KAFKA_GROUP_JOB_PROCESSOR
from application.proto_message import ROLLBACK_PROCESS_CV_COMMAND, SEND_NOTIFICATION_COMMAND, PROCESS_CV_COMMAND, \
    PROCESS_JOB_COMMAND, ROLLBACK_PROCESS_JOB_COMMAND
from infrastructure.elasticsearch_client import ElasticsearchClient
from infrastructure.kafka_producer import KafkaProducer
from infrastructure.minio_client import MinioClient


class DependencyInjection:
    def __init__(self):
        minio_client = MinioClient()
        elasticsearch_client = ElasticsearchClient()
        rollback_process_cv_producer = KafkaProducer(ROLLBACK_PROCESS_CV_COMMAND)
        rollback_process_job_producer = KafkaProducer(ROLLBACK_PROCESS_JOB_COMMAND)
        send_noti_producer = KafkaProducer(SEND_NOTIFICATION_COMMAND)
        self.process_job_consumer = ProcessJobConsumer(
            schema = PROCESS_JOB_COMMAND,
            topic = KAFKA_TOPIC_JOB_COMMAND,
            group_id = KAFKA_GROUP_JOB_PROCESSOR,
            elasticsearch_client = elasticsearch_client,
            rollback_producer = rollback_process_job_producer,
            send_noti_producer = send_noti_producer)

        self.process_cv_consumer = ProcessCvConsumer(
            schema = PROCESS_CV_COMMAND,
            topic = KAFKA_TOPIC_CV_COMMAND,
            group_id = KAFKA_GROUP_CV_PROCESSOR,
            file_storage_client = minio_client,
            rollback_producer = rollback_process_cv_producer,
            send_noti_producer = send_noti_producer)
