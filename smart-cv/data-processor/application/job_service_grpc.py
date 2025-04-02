import logging
import os
from concurrent import futures

import grpc
from dotenv import load_dotenv

import protobuf.job.service_pb2_grpc as grpc_service
from application.proto_message import ExtractedJobData
from cv_parser import extract_job_info

logger = logging.getLogger(__name__)
logging.basicConfig(
    level = logging.INFO,
    format = f"%(asctime)s - {__name__} - %(levelname)s - %(message)s"
)

def job_processor_serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers = 10))
    grpc_service.add_JobProcessorServicer_to_server(JobServiceImpl, server)
    load_dotenv()
    port = os.getenv("JOB_PROCESSOR_PORT")
    server.add_insecure_port("[::]:" + port)
    server.start()
    print("gRPC Server started on port " + port + " ...")
    server.wait_for_termination()

class JobServiceImpl(grpc_service.JobProcessorServicer):

    def __init__(self):
        super().__init__()

    def ExtractData(self, request, context):
        print(f"Received job data for processing: {request}")

        print(request)
        job = request
        data = extract_job_info(job["raw_text"])
        logger.info(data)

        extracted_data = ExtractedJobData(
            email = data["email"],
            phone = data["phone"],
            education = data["educations"],
            skills = data["skills"],
            experience = data["experiences"],
        )
        return extracted_data