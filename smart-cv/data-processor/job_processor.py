from application.job_service_grpc import job_processor_serve
# from infrastructure.elasticsearch_client import ElasticsearchClient

def main():
    # elasticsearch_client = ElasticsearchClient()
    job_processor_serve()

if __name__ == '__main__':
    main()