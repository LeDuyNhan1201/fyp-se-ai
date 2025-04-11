import logging
import os
from concurrent import futures

import grpc
from dotenv import load_dotenv

import protobuf.job.processor_pb2_grpc as grpc_processor
from application.proto_message import ExtractedJobData
from infrastructure.data_parser import DataParser

logger = logging.getLogger(__name__)
logging.basicConfig(
    level = logging.INFO,
    format = f"%(asctime)s - {__name__} - %(levelname)s - %(message)s"
)

def job_processor_serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers = 10))
    grpc_processor.add_JobProcessorServicer_to_server(JobServiceImpl(), server)
    load_dotenv()
    port = os.getenv("JOB_PROCESSOR_PORT")
    server.add_insecure_port("[::]:" + port)
    server.start()
    logger.info("gRPC Server started on port " + port + " ...")
    server.wait_for_termination()

class JobServiceImpl(grpc_processor.JobProcessorServicer):

    def ExtractData(self, request, context):
        logger.info(f"Received job data for processing: {request}")
        job = request

        data_parser = DataParser()
        data = data_parser.parse(job.requirements)
        logger.info(data)

        if not data:
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            context.set_details("Null data of Job description.")
            return ExtractedJobData()

        extracted_data = ExtractedJobData(
            email = data["email"],
            phone = data["phone"],
            educations = data["educations"],
            skills = data["skills"],
            experiences = data["experiences"],
        )

        if len(extracted_data.skills) < 3:
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            context.set_details("Invalid requirements")
            return ExtractedJobData()

        return extracted_data