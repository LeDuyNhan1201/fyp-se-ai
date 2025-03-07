import os
from typing import Union, Any, Optional

from dotenv import load_dotenv
from minio import Minio

load_dotenv()
MINIO_ENDPOINT = os.getenv("MINIO_ENDPOINT")
MINIO_ACCESS_KEY = os.getenv("MINIO_ACCESS_KEY")
MINIO_SECRET_KEY = os.getenv("MINIO_SECRET_KEY")
MINIO_BUCKET_NAME = os.getenv("MINIO_BUCKET_NAME")
DOWNLOAD_FOLDER = os.getenv("DOWNLOAD_FOLDER")

def init_minio_client():
    return Minio(
        MINIO_ENDPOINT,  # Thay bằng địa chỉ MinIO của bạn
        access_key = MINIO_ACCESS_KEY,  # Thay bằng access key của bạn
        secret_key = MINIO_SECRET_KEY,  # Thay bằng secret key của bạn
        secure = False  # True nếu dùng HTTPS, False nếu dùng HTTP
    )

def download_file(object_name) -> Union[Optional[str], Any]:
    if not os.path.exists(DOWNLOAD_FOLDER):
        os.makedirs(DOWNLOAD_FOLDER, exist_ok=True)

    minio_client = init_minio_client()
    download_path = DOWNLOAD_FOLDER + "/" + object_name

    try:
        minio_client.fget_object(MINIO_BUCKET_NAME, object_name, download_path)
        return download_path

    except Exception as e:
        print(f"❌ Unexpected Error: {e}")