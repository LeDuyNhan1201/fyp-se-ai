import logging
import os
from concurrent import futures

import grpc
from dotenv import load_dotenv

import protobuf.cv.service_pb2_grpc as grpc_service
from application.proto_message import ExtractedJobData
from cv_parser import extract_cv_info

logger = logging.getLogger(__name__)
logging.basicConfig(
    level = logging.INFO,
    format = f"%(asctime)s - {__name__} - %(levelname)s - %(message)s"
)

def cv_processor_serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers = 10))
    grpc_service.add_CvProcessorServicer_to_server(CvServiceImpl(), server)
    load_dotenv()
    port = os.getenv("CV_PROCESSOR_PORT")
    server.add_insecure_port("[::]:" + port)
    server.start()
    print("gRPC Server started on port " + port + " ...")
    server.wait_for_termination()

class CvServiceImpl(grpc_service.CvProcessorServicer):

    def ExtractData(self, request, context):
        print(f"Received cv data for processing: {request}")

        print(request)
        cv = request
        data = extract_cv_info(cv.requirements)
        logger.info(data)

        extracted_data = ExtractedJobData(
            name = data["name"],
            email = data["email"],
            phone = data["phone"],
            educations = data["educations"],
            skills = data["skills"],
            experiences = data["experiences"],
        )

        if (len(extracted_data.skills) < 3
                or not len(extracted_data.educations) < 3
                or not extracted_data.name
                or not extracted_data.email
                or not extracted_data.phone):
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            context.set_details("Invalid requirements")
            return ExtractedJobData()

        return extracted_data