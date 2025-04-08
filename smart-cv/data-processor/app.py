from application.cv_service_grpc import cv_processor_serve
from application.job_service_grpc import job_processor_serve
# from infrastructure.elasticsearch_client import ElasticsearchClient

def main():
    # elasticsearch_client = ElasticsearchClient()
    job_processor_serve()
    cv_processor_serve()
    print("App is running")

if __name__ == '__main__':
    main()