import { restClient } from "../rest-client";
import {
  UploadFileSchema
} from "../schemas/file.schema"

export async function uploadFileApi<TResponse>(
  body: UploadFileSchema
): Promise<TResponse> {
  const formData = new FormData();
  formData.append("photo", body.photo);

  const mResponse = await restClient.post<TResponse>(
    "/file",
    formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  }
  );

  return mResponse.data;
}

