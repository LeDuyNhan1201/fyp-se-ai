import logging
import os
from concurrent import futures

import grpc
from dotenv import load_dotenv

import protobuf.cv.processor_pb2_grpc as grpc_service
from application.proto_message import ExtractedCvData
from application.utils import get_file_extensions
from infrastructure.data_parser import DataParser
from infrastructure.document_processor import DocumentProcessor
from infrastructure.minio_client import MinioClient

logger = logging.getLogger(__name__)
logging.basicConfig(
    level = logging.INFO,
    format = f"%(asctime)s - {__name__} - %(levelname)s - %(message)s"
)

def cv_processor_serve(file_storage_client: MinioClient):
    server = grpc.server(futures.ThreadPoolExecutor(max_workers = 10))
    grpc_service.add_CvProcessorServicer_to_server(CvServiceImpl(file_storage_client), server)
    load_dotenv()
    port = os.getenv("CV_PROCESSOR_PORT")
    server.add_insecure_port("[::]:" + port)
    server.start()
    logger.info("gRPC Server started on port " + port + " ...")
    server.wait_for_termination()

class CvServiceImpl(grpc_service.CvProcessorServicer):

    def __init__(self, file_storage_client: MinioClient):
        self.file_storage_client = file_storage_client

    def ExtractData(self, request, context):
        logger.info(f"Received cv data for processing: {request}")
        object_key = request.object_key
        preview_job = request.preview_job
        document_processor = DocumentProcessor()
        file_path = self.file_storage_client.download_file(object_key)
        logger.info(f"FILE EXTENSION: {get_file_extensions(object_key)}")

        try:
            raw_text = document_processor.extract_text_from_pdf(file_path) \
                if get_file_extensions(object_key) == "pdf" \
                else document_processor.extract_text_from_image(file_path)

        except Exception as e:
            logger.error(f"❌ Unexpected Error when read file: {e}")
            context.set_code(grpc.StatusCode.FAILED_PRECONDITION)
            context.set_details("Cannot read CV file.")
            return ExtractedCvData()

        data_parser = DataParser()
        data = data_parser.parse(raw_text, False)
        logger.info(data)

        if not data:
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            context.set_details("Null data of CV file.")
            return ExtractedCvData()

        score = data_parser.calculate_score(
            job_data = {
                "educations": preview_job.educations,
                "skills": preview_job.skills,
                "experiences": preview_job.experiences,
            },
            cv_data = data,
        )
        extracted_data = ExtractedCvData(
            name = data["name"],
            email = data["email"],
            phone = data["phone"],
            educations = data["educations"],
            skills = data["skills"],
            experiences = data["experiences"],
            score = score,
        )

        if (len(extracted_data.skills) < 3
                or len(extracted_data.educations) == 0
                or (not extracted_data.email and not extracted_data.phone)):
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            context.set_details("Invalid requirements")
            return ExtractedCvData()

        return extracted_data