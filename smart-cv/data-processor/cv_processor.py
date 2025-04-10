from application.cv_service_grpc import cv_processor_serve
from infrastructure.minio_client import MinioClient


def main():
    minio_client = MinioClient()
    cv_processor_serve(minio_client)

if __name__ == '__main__':
    main()