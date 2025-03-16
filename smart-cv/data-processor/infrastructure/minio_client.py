import logging
import os
from typing import Union, Any, Optional

from minio import Minio

logger = logging.getLogger(__name__)
logging.basicConfig(
    level = logging.INFO,
    format = f"%(asctime)s - {__name__} - %(levelname)s - %(message)s"
)

class MinioClient:
    def __init__(self):
        self.endpoint = os.getenv("MINIO_ENDPOINT")
        self.access_key = os.getenv("MINIO_ACCESS_KEY")
        self.secret_key = os.getenv("MINIO_SECRET_KEY")
        self.bucket_name = os.getenv("MINIO_BUCKET_NAME")
        self.download_folder = os.getenv("DOWNLOAD_FOLDER")

        self.client = Minio(
            self.endpoint,
            access_key=self.access_key,
            secret_key=self.secret_key,
            secure=False  # True nếu dùng HTTPS, False nếu dùng HTTP
        )

        if not os.path.exists(self.download_folder):
            os.makedirs(self.download_folder, exist_ok=True)

    def download_file(self, object_name: str) -> Union[Optional[str], Any]:
        download_path = os.path.join(self.download_folder, object_name)
        try:
            self.client.fget_object(self.bucket_name, object_name, download_path)
            return download_path
        except Exception as e:
            logger.error(f"❌ Unexpected Error: {e}")
            return None