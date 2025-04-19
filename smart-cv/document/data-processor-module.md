````shell
rm -rf .venv
python -m venv .venv
source .venv/bin/activate.fish

sudo pacman -S tesseract tesseract-data-eng poppler

pip install --upgrade pip
pip install -U pip setuptools wheel
pip install grpcio-tools
pip install python-dotenv
pip install minio
pip install pytesseract pdf2image pillow
pip install scikit-learn sentence-transformers

mkdir protobuf
rm -rf protobuf/*

python -m grpc_tools.protoc --proto_path=./../common/src/main/protobuf \
--python_out=./protobuf \
cv.event.proto

python -m grpc_tools.protoc --proto_path=./../common/src/main/protobuf \
--python_out=./protobuf \
--grpc_python_out=./protobuf \
cv.processor.proto

python -m grpc_tools.protoc --proto_path=./../common/src/main/protobuf \
--python_out=./protobuf \
job.event.proto

python -m grpc_tools.protoc --proto_path=./../common/src/main/protobuf \
--python_out=./protobuf \
--grpc_python_out=./protobuf \
job.processor.proto
````
