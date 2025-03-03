from consumer import init_consumer
from kafka_connector import consumer_config

def main():
    config = consumer_config()
    init_consumer(config)

if __name__ == '__main__':
    main()