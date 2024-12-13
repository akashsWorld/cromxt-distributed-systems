package com.cromxt.file_handler.dtos.requests;

import org.springframework.http.codec.multipart.FilePart;

public record FileUploadRequest(
        FilePart mediaObject
) {
}
