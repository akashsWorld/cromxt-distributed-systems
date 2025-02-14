package com.cromxt.common.server.requests;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaUploadResponse {
    private String fileName;
}
