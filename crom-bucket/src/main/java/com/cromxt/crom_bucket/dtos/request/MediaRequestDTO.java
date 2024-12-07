package com.cromxt.crom_bucket.dtos.request;

import org.springframework.http.codec.multipart.FilePart;

public record MediaRequestDTO(
        FilePart media
) {
}
