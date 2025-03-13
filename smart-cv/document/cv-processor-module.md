````shell
rm -rf .venv
python -m venv .venv

pip install --upgrade pip
pip install -U pip setuptools wheel
pip install grpcio-tools
pip install python-dotenv

pip install confluent_kafka
pip install confluent-kafka[avro,json,protobuf]
pip install minio

sudo pacman -S tesseract tesseract-data-eng poppler
pip install pytesseract pdf2image pillow

pip install -U spacy
python -m spacy download en_core_web_sm
pip install scikit-learn sentence-transformers

mkdir protobuf
rm -rf protobuf/*

python -m grpc_tools.protoc --proto_path=./../common/src/main/protobuf \
--python_out=./protobuf \
notification.command.proto

python -m grpc_tools.protoc --proto_path=./../common/src/main/protobuf \
--python_out=./protobuf \
--grpc_python_out=./protobuf \
cv.command.proto

python -m grpc_tools.protoc --proto_path=./../common/src/main/protobuf \
--python_out=./protobuf \
job.command.proto
````
