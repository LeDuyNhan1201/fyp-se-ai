import os
from elasticsearch import Elasticsearch
from elasticsearch.exceptions import NotFoundError

class ElasticsearchClient:
    def __init__(self):
        """
        Khởi tạo Elasticsearch client.
        :param hosts: Danh sách các host Elasticsearch (ví dụ: ["http://localhost:9200"])
        """
        hosts = [ os.getenv("ELASTICSEARCH_HOSTS") ]
        self.client = Elasticsearch(hosts)

    def create_index(self, index_name: str, mappings: dict = None, settings: dict = None):
        """
        Tạo index mới.
        :param index_name: Tên index.
        :param mappings: Mapping cho index.
        :param settings: Cấu hình cho index.
        :return: Kết quả tạo index.
        """
        if self.client.indices.exists(index=index_name):
            return {"message": "Index already exists"}

        body = {}
        if mappings:
            body["mappings"] = mappings
        if settings:
            body["settings"] = settings

        return self.client.indices.create(index = index_name, body = body)

    def index_document(self, index_name: str, doc_id: str, document: dict):
        """
        Tạo hoặc cập nhật một tài liệu trong index.
        :param index_name: Tên index.
        :param doc_id: ID của tài liệu.
        :param document: Dữ liệu tài liệu.
        :return: Kết quả index tài liệu.
        """
        return self.client.index(index = index_name, id = doc_id, body = document)

    def get_document(self, index_name: str, doc_id: str):
        """
        Lấy thông tin một tài liệu theo ID.
        :param index_name: Tên index.
        :param doc_id: ID của tài liệu.
        :return: Nội dung tài liệu hoặc None nếu không tồn tại.
        """
        try:
            return self.client.get(index = index_name, id = doc_id)["_source"]
        except NotFoundError:
            return None

    def update_document(self, index_name: str, doc_id: str, update_fields: dict):
        """
        Cập nhật một phần tài liệu.
        :param index_name: Tên index.
        :param doc_id: ID tài liệu.
        :param update_fields: Dữ liệu cần cập nhật.
        :return: Kết quả cập nhật.
        """
        return self.client.update(index = index_name, id = doc_id, body = { "doc": update_fields })

    def delete_document(self, index_name: str, doc_id: str):
        """
        Xóa tài liệu khỏi index.
        :param index_name: Tên index.
        :param doc_id: ID tài liệu.
        :return: Kết quả xóa tài liệu.
        """
        try:
            return self.client.delete(index = index_name, id = doc_id)
        except NotFoundError:
            return {"message": "Document not found"}

    def search_documents(self, index_name: str, query: dict, size: int = 10):
        """
        Tìm kiếm tài liệu theo query.
        :param index_name: Tên index.
        :param query: Truy vấn tìm kiếm Elasticsearch.
        :param size: Số lượng kết quả trả về.
        :return: Kết quả tìm kiếm.
        """
        return self.client.search(index = index_name, body = { "query": query, "size": size })["hits"]["hits"]

    def delete_index(self, index_name: str):
        """
        Xóa một index.
        :param index_name: Tên index.
        :return: Kết quả xóa index.
        """
        if not self.client.indices.exists(index = index_name):
            return {"message": "Index does not exist"}
        return self.client.indices.delete(index = index_name)

