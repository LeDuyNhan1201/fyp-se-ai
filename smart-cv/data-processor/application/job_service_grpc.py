import logging
from concurrent import futures

import grpc

import protobuf.job.service_pb2_grpc as grpc_service
from application.proto_message import EXTRACTED_JOB_DATA
from cv_parser import extract_job_info
from infrastructure.elasticsearch_client import ElasticsearchClient

logger = logging.getLogger(__name__)
logging.basicConfig(
    level = logging.INFO,
    format = f"%(asctime)s - {__name__} - %(levelname)s - %(message)s"
)

def serve(elasticsearch_client: ElasticsearchClient):
    server = grpc.server(futures.ThreadPoolExecutor(max_workers = 10))
    grpc_service.add_JobProcessorServicer_to_server(JobServiceImpl(elasticsearch_client), server)
    server.add_insecure_port("[::]:31003")
    server.start()
    print("gRPC Server started on port 31003...")
    server.wait_for_termination()

class JobServiceImpl(grpc_service.JobProcessorServicer):

    def __init__(self, elasticsearch_client: ElasticsearchClient):
        super().__init__()
        self.elasticsearch_client = elasticsearch_client

    def ExtractData(self, request, context):
        print(f"Received job data for processing: {request}")

        print(request)
        job = self.elasticsearch_client.get_document("jobs", request.job_id)
        data = extract_job_info(job["raw_text"])
        logger.info(data)

        extracted_data = EXTRACTED_JOB_DATA(
            email = data["email"],
            phone = data["phone"],
            education = data["education"],
            skills = data["skills"],
            experience = data["experience"],
        )
        return extracted_data