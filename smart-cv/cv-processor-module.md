python -m venv .venv

pip install grpcio-tools

pip install confluent_kafka

pip install python-dotenv

mkdir protobuf
rm -rf protobuf/*

python -m grpc_tools.protoc --proto_path=./../common/src/main/protobuf \
--python_out=./protobuf \
notification.command.proto

python -m grpc_tools.protoc --proto_path=./../common/src/main/protobuf \
--python_out=./protobuf \
--grpc_python_out=./protobuf \
cv.command.proto
