import axios from "axios";

const apiClient = axios.create({
    baseURL: "http://localhost:8080"
});

const submitInputFile = (file, onUploadProgress) => {
    let formData = new FormData();

    formData.append("file", file);

    return apiClient.post("/delivery/file", formData, {
        headers: {
            "Content-Type": "multipart/form-data",
        },
        responseType: 'blob',
        onUploadProgress,
    });
}

export const ApiService = {
    submitInputFile
}
